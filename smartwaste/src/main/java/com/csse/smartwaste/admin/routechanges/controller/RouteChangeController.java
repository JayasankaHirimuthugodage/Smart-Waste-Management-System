package com.csse.smartwaste.admin.routechanges.controller;

import java.util.Map;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.csse.smartwaste.admin.routechanges.model.RouteChange;
import com.csse.smartwaste.admin.routechanges.service.RouteChangeService;

@RestController
@RequestMapping("/api/admin/routes")
@CrossOrigin(origins = "*")
public class RouteChangeController {

    private final RouteChangeService routeService;

    public RouteChangeController(RouteChangeService routeService) {
        this.routeService = routeService;
    }

    @GetMapping
    public ResponseEntity<List<RouteChange>> getAll() {
        return ResponseEntity.ok(routeService.getAllRequests());
    }

    @PostMapping
    public ResponseEntity<RouteChange> create(@RequestBody RouteChange request) {
        return ResponseEntity.ok(routeService.createRequest(request));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable String id, @RequestParam String status) {
        try {
            RouteChange updated = routeService.updateStatus(id, status);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Unexpected error"));
        }
    }

    @GetMapping("/suggestions")
    public ResponseEntity<List<RouteChange>> getHighWasteSuggestions() {
        return ResponseEntity.ok(routeService.getHighWasteAreaSuggestions());
    }
}
