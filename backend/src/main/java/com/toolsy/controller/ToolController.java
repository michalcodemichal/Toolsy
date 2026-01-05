package com.toolsy.controller;

import com.toolsy.dto.request.CreateToolRequest;
import com.toolsy.dto.response.PagedResponse;
import com.toolsy.dto.response.ToolResponse;
import com.toolsy.service.ToolService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/tools")
public class ToolController {
    private final ToolService toolService;

    @Autowired
    public ToolController(ToolService toolService) {
        this.toolService = toolService;
    }

    @GetMapping
    public ResponseEntity<?> getAllTools(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String sortOrder) {
        
        if (page != null && size != null) {
            PagedResponse<ToolResponse> pagedResponse = toolService.getAllToolsPaginated(page, size, sortBy, sortOrder);
            return ResponseEntity.ok(pagedResponse);
        }
        
        List<ToolResponse> tools = toolService.getAllTools();
        return ResponseEntity.ok(tools);
    }

    @GetMapping("/available")
    public ResponseEntity<?> getAvailableTools(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String sortOrder) {
        
        if (page != null && size != null) {
            PagedResponse<ToolResponse> pagedResponse = toolService.getAvailableToolsPaginated(page, size, sortBy, sortOrder);
            return ResponseEntity.ok(pagedResponse);
        }
        
        List<ToolResponse> tools = toolService.getAvailableTools();
        return ResponseEntity.ok(tools);
    }

    @GetMapping("/available-for-period")
    public ResponseEntity<List<ToolResponse>> getAvailableToolsForPeriod(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        System.out.println("=== getAvailableToolsForPeriod called ===");
        System.out.println("startDate: " + startDate);
        System.out.println("endDate: " + endDate);
        
        if (startDate != null && endDate != null) {
            try {
                java.time.LocalDate start = java.time.LocalDate.parse(startDate);
                java.time.LocalDate end = java.time.LocalDate.parse(endDate);
                System.out.println("Parsed dates - start: " + start + ", end: " + end);
                List<ToolResponse> tools = toolService.getAvailableToolsForPeriod(start, end);
                System.out.println("Found " + tools.size() + " available tools");
                return ResponseEntity.ok(tools);
            } catch (Exception e) {
                System.err.println("Error parsing dates: " + e.getMessage());
                e.printStackTrace();
                // Jeśli błąd parsowania dat, zwróć wszystkie dostępne narzędzia
                List<ToolResponse> tools = toolService.getAvailableTools();
                return ResponseEntity.ok(tools);
            }
        }
        // Jeśli brak dat, zwróć wszystkie dostępne narzędzia
        System.out.println("No dates provided, returning all available tools");
        List<ToolResponse> tools = toolService.getAvailableTools();
        return ResponseEntity.ok(tools);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<?> getToolsByCategory(
            @PathVariable String category,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String sortOrder) {
        
        if (page != null && size != null) {
            PagedResponse<ToolResponse> pagedResponse = toolService.getToolsByCategoryPaginated(category, page, size, sortBy, sortOrder);
            return ResponseEntity.ok(pagedResponse);
        }
        
        List<ToolResponse> tools = toolService.getToolsByCategory(category);
        return ResponseEntity.ok(tools);
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchTools(
            @RequestParam String q,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String sortOrder) {
        
        if (page != null && size != null) {
            PagedResponse<ToolResponse> pagedResponse = toolService.searchToolsPaginated(q, page, size, sortBy, sortOrder);
            return ResponseEntity.ok(pagedResponse);
        }
        
        List<ToolResponse> tools = toolService.searchTools(q);
        return ResponseEntity.ok(tools);
    }

    @GetMapping("/sorted")
    public ResponseEntity<List<ToolResponse>> getToolsSorted(
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String sortOrder) {
        List<ToolResponse> tools = toolService.getToolsSorted(sortBy, sortOrder);
        return ResponseEntity.ok(tools);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ToolResponse> getToolById(
            @PathVariable Long id,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        if (startDate != null && endDate != null) {
            try {
                java.time.LocalDate start = java.time.LocalDate.parse(startDate);
                java.time.LocalDate end = java.time.LocalDate.parse(endDate);
                ToolResponse tool = toolService.getToolByIdWithAvailability(id, start, end);
                return ResponseEntity.ok(tool);
            } catch (Exception e) {
                ToolResponse tool = toolService.getToolById(id);
                return ResponseEntity.ok(tool);
            }
        }
        ToolResponse tool = toolService.getToolById(id);
        return ResponseEntity.ok(tool);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ToolResponse> createTool(@Valid @RequestBody CreateToolRequest request) {
        ToolResponse tool = toolService.createTool(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(tool);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ToolResponse> updateTool(@PathVariable Long id, @Valid @RequestBody CreateToolRequest request) {
        ToolResponse tool = toolService.updateTool(id, request);
        return ResponseEntity.ok(tool);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteTool(@PathVariable Long id) {
        toolService.deleteTool(id);
        return ResponseEntity.noContent().build();
    }
}

