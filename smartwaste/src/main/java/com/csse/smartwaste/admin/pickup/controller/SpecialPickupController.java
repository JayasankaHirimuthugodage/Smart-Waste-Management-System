package com.csse.smartwaste.admin.pickup.controller;

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

import com.csse.smartwaste.admin.pickup.entity.SpecialPickup;
import com.csse.smartwaste.admin.pickup.service.SpecialPickupService;

@RestController
@RequestMapping("/api/admin/pickups")
@CrossOrigin(origins = "*")
public class SpecialPickupController {

    private final SpecialPickupService pickupService;

    public SpecialPickupController(SpecialPickupService pickupService) {
        this.pickupService = pickupService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<SpecialPickup>> getAll() {
        return ResponseEntity.ok(pickupService.getAllRequests());
    }

    @PostMapping("/create")
    public ResponseEntity<SpecialPickup> create(@RequestBody SpecialPickup request) {
        return ResponseEntity.ok(pickupService.createRequest(request));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable String id, @RequestParam String status) {
        SpecialPickup updated = pickupService.updateStatus(id, status);
        if (updated == null) {
            return ResponseEntity.status(404).body(
                    java.util.Map.of("error", "Pickup not found or could not update", "id", id)
            );
        }
        return ResponseEntity.ok(updated);
    }

}
