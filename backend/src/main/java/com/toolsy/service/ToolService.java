package com.toolsy.service;

import com.toolsy.dto.request.CreateToolRequest;
import com.toolsy.dto.response.ToolResponse;
import com.toolsy.model.Tool;
import com.toolsy.model.ToolStatus;
import com.toolsy.model.Rental;
import com.toolsy.model.RentalStatus;
import com.toolsy.repository.ToolRepository;
import com.toolsy.repository.RentalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ToolService {
    private final ToolRepository toolRepository;
    private final RentalRepository rentalRepository;

    @Autowired
    public ToolService(ToolRepository toolRepository, RentalRepository rentalRepository) {
        this.toolRepository = toolRepository;
        this.rentalRepository = rentalRepository;
    }

    public ToolResponse createTool(CreateToolRequest request) {
        Tool tool = new Tool();
        tool.setName(request.getName());
        tool.setDescription(request.getDescription());
        tool.setCategory(request.getCategory());
        tool.setDailyPrice(request.getDailyPrice());
        tool.setQuantity(request.getQuantity());
        if (request.getImageUrl() != null && !request.getImageUrl().trim().isEmpty()) {
            tool.setImageUrl(request.getImageUrl().trim());
        } else {
            tool.setImageUrl(null);
        }
        tool.setStatus(ToolStatus.AVAILABLE);

        Tool savedTool = toolRepository.save(tool);
        return mapToResponse(savedTool);
    }

    public List<ToolResponse> getAllTools() {
        List<Tool> allTools = toolRepository.findAll();
        System.out.println("Pobrano " + allTools.size() + " narzędzi z bazy");
        return allTools.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<ToolResponse> getAvailableTools() {
        return toolRepository.findAvailableTools(ToolStatus.AVAILABLE).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<ToolResponse> getAvailableToolsForPeriod(LocalDate startDate, LocalDate endDate) {
        List<Tool> allTools = toolRepository.findAvailableTools(ToolStatus.AVAILABLE);
        
        return allTools.stream()
                .map(tool -> {
                    ToolResponse response = mapToResponse(tool);
                    int availableQuantity = calculateAvailableQuantity(tool, startDate, endDate);
                    response.setQuantity(availableQuantity);
                    return response;
                })
                .filter(tool -> tool.getQuantity() > 0) 
                .collect(Collectors.toList());
    }

    public ToolResponse getToolById(Long id) {
        Tool tool = toolRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Narzędzie nie znalezione"));
        return mapToResponse(tool);
    }

    public ToolResponse getToolByIdWithAvailability(Long id, java.time.LocalDate startDate, java.time.LocalDate endDate) {
        Tool tool = toolRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Narzędzie nie znalezione"));
        ToolResponse response = mapToResponse(tool);
        
        if (startDate != null && endDate != null) {
            int availableQuantity = calculateAvailableQuantity(tool, startDate, endDate);
            response.setQuantity(availableQuantity);
        }
        
        return response;
    }

    private int calculateAvailableQuantity(Tool tool, LocalDate startDate, LocalDate endDate) {
        List<Rental> overlappingRentals = rentalRepository.findOverlappingRentals(
                tool,
                startDate,
                endDate,
                List.of(RentalStatus.PENDING, RentalStatus.ACTIVE)
        );
        
        int rentedQuantity = overlappingRentals.stream()
                .mapToInt(r -> r.getQuantity() != null ? r.getQuantity() : 1)
                .sum();
        
        return Math.max(0, tool.getQuantity() - rentedQuantity);
    }

    public List<ToolResponse> getToolsByCategory(String category) {
        return toolRepository.findByCategory(category).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<ToolResponse> searchTools(String searchTerm) {
        return toolRepository.searchTools(searchTerm).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<ToolResponse> getToolsSorted(String sortBy, String sortOrder) {
        List<Tool> tools = toolRepository.findAll();
        
        if (sortBy != null && !sortBy.isEmpty()) {
            tools.sort((a, b) -> {
                int result = 0;
                switch (sortBy.toLowerCase()) {
                    case "name":
                        result = a.getName().compareToIgnoreCase(b.getName());
                        break;
                    case "price":
                        result = a.getDailyPrice().compareTo(b.getDailyPrice());
                        break;
                    case "category":
                        result = a.getCategory().compareToIgnoreCase(b.getCategory());
                        break;
                    default:
                        return 0;
                }
                return "desc".equalsIgnoreCase(sortOrder) ? -result : result;
            });
        }
        
        return tools.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public ToolResponse updateTool(Long id, CreateToolRequest request) {
        Tool tool = toolRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Narzędzie nie znalezione"));

        tool.setName(request.getName());
        tool.setDescription(request.getDescription());
        tool.setCategory(request.getCategory());
        tool.setDailyPrice(request.getDailyPrice());
        tool.setQuantity(request.getQuantity());
        if (request.getImageUrl() != null && !request.getImageUrl().trim().isEmpty()) {
            tool.setImageUrl(request.getImageUrl().trim());
        } else {
            tool.setImageUrl(null);
        }

        Tool updatedTool = toolRepository.save(tool);
        return mapToResponse(updatedTool);
    }

    public void deleteTool(Long id) {
        Tool tool = toolRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Narzędzie nie znalezione"));
        toolRepository.delete(tool);
    }

    public Tool getToolEntity(Long id) {
        return toolRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Narzędzie nie znalezione"));
    }

    public Tool saveTool(Tool tool) {
        return toolRepository.save(tool);
    }

    private ToolResponse mapToResponse(Tool tool) {
        ToolResponse response = new ToolResponse();
        response.setId(tool.getId());
        response.setName(tool.getName());
        response.setDescription(tool.getDescription());
        response.setCategory(tool.getCategory());
        response.setDailyPrice(tool.getDailyPrice());
        response.setQuantity(tool.getQuantity());
        response.setImageUrl(tool.getImageUrl());
        response.setStatus(tool.getStatus());
        response.setCreatedAt(tool.getCreatedAt());
        response.setUpdatedAt(tool.getUpdatedAt());
        return response;
    }
}

