package com.toolsy.controller;

import com.toolsy.service.FileStorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/files")
@Tag(name = "Pliki", description = "API do zarządzania plikami")
public class FileController {
    private final FileStorageService fileStorageService;

    @Autowired
    public FileController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @PostMapping("/upload")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Prześlij plik", description = "Przesyła plik (np. zdjęcie narzędzia) do serwera (wymaga uprawnień ADMIN)", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Plik przesłany pomyślnie"),
        @ApiResponse(responseCode = "400", description = "Błąd przesyłania pliku"),
        @ApiResponse(responseCode = "401", description = "Brak autoryzacji"),
        @ApiResponse(responseCode = "403", description = "Brak uprawnień")
    })
    public ResponseEntity<Map<String, String>> uploadFile(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Plik do przesłania", required = true, content = @Content(mediaType = "multipart/form-data")) @RequestParam("file") MultipartFile file) {
        try {
            String fileUrl = fileStorageService.storeFile(file);
            Map<String, String> response = new HashMap<>();
            response.put("url", fileUrl);
            response.put("message", "Plik przesłany pomyślnie");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Błąd przesyłania pliku: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
}







