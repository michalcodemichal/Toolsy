package com.toolsy.service;

import com.toolsy.dto.request.CreateToolRequest;
import com.toolsy.dto.response.PagedResponse;
import com.toolsy.dto.response.ToolResponse;
import com.toolsy.model.Tool;
import com.toolsy.model.ToolStatus;
import com.toolsy.model.Rental;
import com.toolsy.model.RentalStatus;
import com.toolsy.repository.ToolRepository;
import com.toolsy.repository.RentalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    private final ReviewService reviewService;

    @Autowired
    public ToolService(ToolRepository toolRepository, RentalRepository rentalRepository, @Lazy ReviewService reviewService) {
        this.toolRepository = toolRepository;
        this.rentalRepository = rentalRepository;
        this.reviewService = reviewService;
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

    public PagedResponse<ToolResponse> getAllToolsPaginated(int page, int size, String sortBy, String sortOrder) {
        Pageable pageable = createPageable(page, size, sortBy, sortOrder);
        Page<Tool> toolPage = toolRepository.findAll(pageable);
        
        return mapToPagedResponse(toolPage);
    }

    public PagedResponse<ToolResponse> getAvailableToolsPaginated(int page, int size, String sortBy, String sortOrder) {
        Pageable pageable = createPageable(page, size, sortBy, sortOrder);
        Page<Tool> toolPage = toolRepository.findAvailableToolsPageable(ToolStatus.AVAILABLE, pageable);
        
        return mapToPagedResponse(toolPage);
    }

    public PagedResponse<ToolResponse> searchToolsPaginated(String searchTerm, int page, int size, String sortBy, String sortOrder) {
        Pageable pageable = createPageable(page, size, sortBy, sortOrder);
        Page<Tool> toolPage = toolRepository.searchToolsPageable(searchTerm, pageable);
        
        return mapToPagedResponse(toolPage);
    }

    public PagedResponse<ToolResponse> getToolsByCategoryPaginated(String category, int page, int size, String sortBy, String sortOrder) {
        Pageable pageable = createPageable(page, size, sortBy, sortOrder);
        Page<Tool> toolPage = toolRepository.findByCategory(category, pageable);
        
        return mapToPagedResponse(toolPage);
    }

    private Pageable createPageable(int page, int size, String sortBy, String sortOrder) {
        Sort sort = Sort.unsorted();
        
        if (sortBy != null && !sortBy.isEmpty()) {
            Sort.Direction direction = "desc".equalsIgnoreCase(sortOrder) ? Sort.Direction.DESC : Sort.Direction.ASC;
            
            switch (sortBy.toLowerCase()) {
                case "name":
                    sort = Sort.by(direction, "name");
                    break;
                case "price":
                    sort = Sort.by(direction, "dailyPrice");
                    break;
                case "category":
                    sort = Sort.by(direction, "category");
                    break;
                default:
                    sort = Sort.by(Sort.Direction.ASC, "id");
            }
        } else {
            sort = Sort.by(Sort.Direction.ASC, "id");
        }
        
        return PageRequest.of(page, size, sort);
    }

    private PagedResponse<ToolResponse> mapToPagedResponse(Page<Tool> toolPage) {
        List<ToolResponse> content = toolPage.getContent().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        
        return new PagedResponse<>(
                content,
                toolPage.getNumber(),
                toolPage.getSize(),
                toolPage.getTotalElements(),
                toolPage.getTotalPages(),
                toolPage.isFirst(),
                toolPage.isLast()
        );
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

    public void updateToolRating(Long toolId) {
        // This method is called by ReviewService to update tool ratings
        // Implementation can be added if needed for caching or other optimizations
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
        
        // Add review statistics if ReviewService is available
        if (reviewService != null) {
            response.setAverageRating(reviewService.getAverageRating(tool.getId()));
            response.setReviewCount(reviewService.getReviewCount(tool.getId()));
        } else {
            response.setAverageRating(java.math.BigDecimal.ZERO);
            response.setReviewCount(0L);
        }
        
        return response;
    }
}

