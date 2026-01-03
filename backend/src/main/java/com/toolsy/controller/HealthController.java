package com.toolsy.controller;

import com.toolsy.repository.ToolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/health")
public class HealthController {
    private final ToolRepository toolRepository;

    @Autowired
    public HealthController(ToolRepository toolRepository) {
        this.toolRepository = toolRepository;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "OK");
        response.put("toolCount", toolRepository.count());
        return ResponseEntity.ok(response);
    }
}


