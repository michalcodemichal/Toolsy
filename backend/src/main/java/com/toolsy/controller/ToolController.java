package com.toolsy.controller;

import com.toolsy.dto.request.CreateToolRequest;
import com.toolsy.dto.response.PagedResponse;
import com.toolsy.dto.response.ToolResponse;
import com.toolsy.service.ToolService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Narzędzia", description = "API do zarządzania narzędziami")
public class ToolController {
    private final ToolService toolService;

    @Autowired
    public ToolController(ToolService toolService) {
        this.toolService = toolService;
    }

    @GetMapping
    @Operation(summary = "Pobierz wszystkie narzędzia", description = "Zwraca listę wszystkich narzędzi z opcjonalną paginacją i sortowaniem")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista narzędzi", content = @Content(schema = @Schema(implementation = ToolResponse.class)))
    })
    public ResponseEntity<?> getAllTools(
            @Parameter(description = "Numer strony (dla paginacji)") @RequestParam(required = false) Integer page,
            @Parameter(description = "Rozmiar strony (dla paginacji)") @RequestParam(required = false) Integer size,
            @Parameter(description = "Pole do sortowania") @RequestParam(required = false) String sortBy,
            @Parameter(description = "Kierunek sortowania (asc/desc)") @RequestParam(required = false, defaultValue = "asc") String sortOrder) {
        
        if (page != null && size != null) {
            PagedResponse<ToolResponse> pagedResponse = toolService.getAllToolsPaginated(page, size, sortBy, sortOrder);
            return ResponseEntity.ok(pagedResponse);
        }
        
        List<ToolResponse> tools = toolService.getAllTools();
        return ResponseEntity.ok(tools);
    }

    @GetMapping("/available")
    @Operation(summary = "Pobierz dostępne narzędzia", description = "Zwraca listę narzędzi dostępnych do wypożyczenia")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista dostępnych narzędzi", content = @Content(schema = @Schema(implementation = ToolResponse.class)))
    })
    public ResponseEntity<?> getAvailableTools(
            @Parameter(description = "Numer strony (dla paginacji)") @RequestParam(required = false) Integer page,
            @Parameter(description = "Rozmiar strony (dla paginacji)") @RequestParam(required = false) Integer size,
            @Parameter(description = "Pole do sortowania") @RequestParam(required = false) String sortBy,
            @Parameter(description = "Kierunek sortowania (asc/desc)") @RequestParam(required = false, defaultValue = "asc") String sortOrder) {
        
        if (page != null && size != null) {
            PagedResponse<ToolResponse> pagedResponse = toolService.getAvailableToolsPaginated(page, size, sortBy, sortOrder);
            return ResponseEntity.ok(pagedResponse);
        }
        
        List<ToolResponse> tools = toolService.getAvailableTools();
        return ResponseEntity.ok(tools);
    }

    @GetMapping("/available-for-period")
    @Operation(summary = "Pobierz dostępne narzędzia w okresie", description = "Zwraca narzędzia dostępne w określonym przedziale dat")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista dostępnych narzędzi", content = @Content(schema = @Schema(implementation = ToolResponse.class)))
    })
    public ResponseEntity<List<ToolResponse>> getAvailableToolsForPeriod(
            @Parameter(description = "Data rozpoczęcia (format: YYYY-MM-DD)") @RequestParam(required = false) String startDate,
            @Parameter(description = "Data zakończenia (format: YYYY-MM-DD)") @RequestParam(required = false) String endDate) {
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
                List<ToolResponse> tools = toolService.getAvailableTools();
                return ResponseEntity.ok(tools);
            }
        }
        System.out.println("No dates provided, returning all available tools");
        List<ToolResponse> tools = toolService.getAvailableTools();
        return ResponseEntity.ok(tools);
    }

    @GetMapping("/category/{category}")
    @Operation(summary = "Pobierz narzędzia po kategorii", description = "Zwraca narzędzia z określonej kategorii")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista narzędzi w kategorii", content = @Content(schema = @Schema(implementation = ToolResponse.class)))
    })
    public ResponseEntity<?> getToolsByCategory(
            @Parameter(description = "Kategoria narzędzi") @PathVariable String category,
            @Parameter(description = "Numer strony (dla paginacji)") @RequestParam(required = false) Integer page,
            @Parameter(description = "Rozmiar strony (dla paginacji)") @RequestParam(required = false) Integer size,
            @Parameter(description = "Pole do sortowania") @RequestParam(required = false) String sortBy,
            @Parameter(description = "Kierunek sortowania (asc/desc)") @RequestParam(required = false, defaultValue = "asc") String sortOrder) {
        
        if (page != null && size != null) {
            PagedResponse<ToolResponse> pagedResponse = toolService.getToolsByCategoryPaginated(category, page, size, sortBy, sortOrder);
            return ResponseEntity.ok(pagedResponse);
        }
        
        List<ToolResponse> tools = toolService.getToolsByCategory(category);
        return ResponseEntity.ok(tools);
    }

    @GetMapping("/search")
    @Operation(summary = "Wyszukaj narzędzia", description = "Wyszukuje narzędzia po nazwie lub opisie")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista znalezionych narzędzi", content = @Content(schema = @Schema(implementation = ToolResponse.class)))
    })
    public ResponseEntity<?> searchTools(
            @Parameter(description = "Zapytanie wyszukiwania", required = true) @RequestParam String q,
            @Parameter(description = "Numer strony (dla paginacji)") @RequestParam(required = false) Integer page,
            @Parameter(description = "Rozmiar strony (dla paginacji)") @RequestParam(required = false) Integer size,
            @Parameter(description = "Pole do sortowania") @RequestParam(required = false) String sortBy,
            @Parameter(description = "Kierunek sortowania (asc/desc)") @RequestParam(required = false, defaultValue = "asc") String sortOrder) {
        
        if (page != null && size != null) {
            PagedResponse<ToolResponse> pagedResponse = toolService.searchToolsPaginated(q, page, size, sortBy, sortOrder);
            return ResponseEntity.ok(pagedResponse);
        }
        
        List<ToolResponse> tools = toolService.searchTools(q);
        return ResponseEntity.ok(tools);
    }

    @GetMapping("/sorted")
    @Operation(summary = "Pobierz posortowane narzędzia", description = "Zwraca wszystkie narzędzia posortowane według określonego kryterium")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista posortowanych narzędzi", content = @Content(schema = @Schema(implementation = ToolResponse.class)))
    })
    public ResponseEntity<List<ToolResponse>> getToolsSorted(
            @Parameter(description = "Pole do sortowania") @RequestParam(required = false) String sortBy,
            @Parameter(description = "Kierunek sortowania (asc/desc)") @RequestParam(required = false, defaultValue = "asc") String sortOrder) {
        List<ToolResponse> tools = toolService.getToolsSorted(sortBy, sortOrder);
        return ResponseEntity.ok(tools);
    }

    @GetMapping("/categories")
    @Operation(summary = "Pobierz wszystkie kategorie", description = "Zwraca listę wszystkich unikalnych kategorii narzędzi")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista kategorii", content = @Content(schema = @Schema(implementation = String.class)))
    })
    public ResponseEntity<List<String>> getAllCategories() {
        List<String> categories = toolService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Pobierz szczegóły narzędzia", description = "Zwraca szczegóły narzędzia o podanym ID z opcjonalną informacją o dostępności w okresie")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Szczegóły narzędzia", content = @Content(schema = @Schema(implementation = ToolResponse.class))),
        @ApiResponse(responseCode = "404", description = "Narzędzie nie znalezione")
    })
    public ResponseEntity<ToolResponse> getToolById(
            @Parameter(description = "ID narzędzia", required = true) @PathVariable Long id,
            @Parameter(description = "Data rozpoczęcia (format: YYYY-MM-DD)") @RequestParam(required = false) String startDate,
            @Parameter(description = "Data zakończenia (format: YYYY-MM-DD)") @RequestParam(required = false) String endDate) {
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
    @Operation(summary = "Utwórz nowe narzędzie", description = "Tworzy nowe narzędzie w systemie (wymaga uprawnień ADMIN)", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Narzędzie utworzone pomyślnie", content = @Content(schema = @Schema(implementation = ToolResponse.class))),
        @ApiResponse(responseCode = "400", description = "Nieprawidłowe dane wejściowe"),
        @ApiResponse(responseCode = "401", description = "Brak autoryzacji"),
        @ApiResponse(responseCode = "403", description = "Brak uprawnień")
    })
    public ResponseEntity<ToolResponse> createTool(@Valid @RequestBody CreateToolRequest request) {
        ToolResponse tool = toolService.createTool(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(tool);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Aktualizuj narzędzie", description = "Aktualizuje istniejące narzędzie (wymaga uprawnień ADMIN)", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Narzędzie zaktualizowane pomyślnie", content = @Content(schema = @Schema(implementation = ToolResponse.class))),
        @ApiResponse(responseCode = "400", description = "Nieprawidłowe dane wejściowe"),
        @ApiResponse(responseCode = "404", description = "Narzędzie nie znalezione"),
        @ApiResponse(responseCode = "401", description = "Brak autoryzacji"),
        @ApiResponse(responseCode = "403", description = "Brak uprawnień")
    })
    public ResponseEntity<ToolResponse> updateTool(@Parameter(description = "ID narzędzia", required = true) @PathVariable Long id, @Valid @RequestBody CreateToolRequest request) {
        ToolResponse tool = toolService.updateTool(id, request);
        return ResponseEntity.ok(tool);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Usuń narzędzie", description = "Usuwa narzędzie z systemu (wymaga uprawnień ADMIN)", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Narzędzie usunięte pomyślnie"),
        @ApiResponse(responseCode = "404", description = "Narzędzie nie znalezione"),
        @ApiResponse(responseCode = "401", description = "Brak autoryzacji"),
        @ApiResponse(responseCode = "403", description = "Brak uprawnień")
    })
    public ResponseEntity<Void> deleteTool(@Parameter(description = "ID narzędzia", required = true) @PathVariable Long id) {
        toolService.deleteTool(id);
        return ResponseEntity.noContent().build();
    }
}

