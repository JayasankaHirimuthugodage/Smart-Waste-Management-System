package com.csse.smartwaste.admin.routechanges.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.csse.smartwaste.admin.routechanges.model.RouteChange;
import com.csse.smartwaste.admin.routechanges.repository.RouteChangeRepository;
import com.csse.smartwaste.admin.routechanges.service.RouteChangeService;

@Service
public class RouteChangeServiceImpl implements RouteChangeService {

    private final RouteChangeRepository repository;

    public RouteChangeServiceImpl(RouteChangeRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<RouteChange> getAllRequests() {
        return repository.findAll();
    }

    @Override
    public RouteChange createRequest(RouteChange request) {
        request.setStatus("PENDING");
        request.setDateRequested(LocalDateTime.now());

        // Assign mock defaults for demo consistency
        if (request.getCurrentStops() == null || request.getCurrentStops().isEmpty()) {
            request.setCurrentStops(List.of("Stop A", "Stop B", "Stop C"));
        }
        if (request.getProposedStops() == null || request.getProposedStops().isEmpty()) {
            request.setProposedStops(List.of("Stop A", "Stop D (Optimized)", "Stop C"));
        }
        if (request.getReason() == null || request.getReason().isEmpty()) {
            request.setReason("Optimization to reduce travel distance and balance bin loads.");
        }
        if (request.getOptimizationType() == null) {
            request.setOptimizationType("Distance Optimization");
        }

        request.setCurrentDistanceKm(14.5);
        request.setProposedDistanceKm(10.9);
        request.setWasteVolumePerDay(950);
        request.setPriority("HIGH");
        request.setRequestedBy("Analytics Engine");

        return repository.save(request);
    }

    @Override
    public RouteChange updateStatus(String id, String status) {
        RouteChange existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Route not found"));

        existing.setStatus(status.toUpperCase());
        if (status.equalsIgnoreCase("APPROVED")) {
            existing.setEffectiveFrom(LocalDateTime.now());
        }
        return repository.save(existing);
    }

    @Override
    public List<RouteChange> getHighWasteAreaSuggestions() {
        Random rand = new Random();
        return repository.findAll().stream()
                .filter(r -> r.getWasteVolumePerDay() > 800)
                .filter(r -> r.getStatus().equalsIgnoreCase("PENDING"))
                .peek(r -> r.setReason("High waste detected — suggest optimization"))
                .limit(5)
                .collect(Collectors.toList());
    }
}
