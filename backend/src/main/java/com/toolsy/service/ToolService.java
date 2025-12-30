package com.toolsy.service;

import com.toolsy.dto.request.CreateToolRequest;
import com.toolsy.dto.response.ToolResponse;
import com.toolsy.model.Tool;
import com.toolsy.model.ToolStatus;
import com.toolsy.repository.ToolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ToolService {
    private final ToolRepository toolRepository;

    @Autowired
    public ToolService(ToolRepository toolRepository) {
        this.toolRepository = toolRepository;
    }

    public ToolResponse createTool(CreateToolRequest request) {
        Tool tool = new Tool();
        tool.setName(request.getName());
        tool.setDescription(request.getDescription());
        tool.setCategory(request.getCategory());
        tool.setDailyPrice(request.getDailyPrice());
        tool.setQuantity(request.getQuantity());
        tool.setImageUrl(request.getImageUrl());
        tool.setStatus(ToolStatus.AVAILABLE);

        Tool savedTool = toolRepository.save(tool);
        return mapToResponse(savedTool);
    }

    public List<ToolResponse> getAllTools() {
        return toolRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<ToolResponse> getAvailableTools() {
        return toolRepository.findAvailableTools(ToolStatus.AVAILABLE).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public ToolResponse getToolById(Long id) {
        Tool tool = toolRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Narzędzie nie znalezione"));
        return mapToResponse(tool);
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

    public ToolResponse updateTool(Long id, CreateToolRequest request) {
        Tool tool = toolRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Narzędzie nie znalezione"));

        tool.setName(request.getName());
        tool.setDescription(request.getDescription());
        tool.setCategory(request.getCategory());
        tool.setDailyPrice(request.getDailyPrice());
        tool.setQuantity(request.getQuantity());
        if (request.getImageUrl() != null) {
            tool.setImageUrl(request.getImageUrl());
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

