package com.csse.smartwaste.admin.adminanalytics.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.csse.smartwaste.admin.adminanalytics.dto.PerformanceAnalyticsDto;
import com.csse.smartwaste.admin.adminanalytics.service.PerformanceAnalyticsService;

/**
 * PerformanceAnalyticsServiceImpl
 *
 * SRP: Handles waste collection performance metrics (IoT + route data). OCP:
 * Supports extension for new KPIs. DIP: Injected wherever
 * PerformanceAnalyticsService is required.
 */
@Service
public class PerformanceAnalyticsServiceImpl implements PerformanceAnalyticsService {

    @Override
    public PerformanceAnalyticsDto generatePerformanceAnalytics() {
        PerformanceAnalyticsDto dto = new PerformanceAnalyticsDto();

        // Simulated performance metrics (can later be replaced with IoT data)
        dto.setCompletionRate(94.5);
        dto.setAvgCollectionTime(18.0);
        dto.setWorkerEfficiency(87.0);
        dto.setMissedCollections(12);

        // Mock Top Routes
        dto.setTopRoutes(List.of(
                createRoute("Route A - Downtown", 145, "96%", "15 min", "Excellent"),
                createRoute("Route B - North Zone", 203, "92%", "19 min", "Good"),
                createRoute("Route C - Industrial Area", 176, "89%", "21 min", "Moderate")
        ));

        // Mock Top Workers
        dto.setTopWorkers(List.of(
                createWorker("John Smith", 234, "95%", 4.8),
                createWorker("Sarah Johnson", 228, "94%", 4.7),
                createWorker("Michael Brown", 210, "91%", 4.6)
        ));

        return dto;
    }

    // -------------------- Helper Methods --------------------
    private PerformanceAnalyticsDto.RoutePerformance createRoute(String route, int collections, String efficiency, String avgTime, String status) {
        PerformanceAnalyticsDto.RoutePerformance r = new PerformanceAnalyticsDto.RoutePerformance();
        r.setRoute(route);
        r.setCollections(collections);
        r.setEfficiency(efficiency);
        r.setAvgTime(avgTime);
        r.setStatus(status);
        return r;
    }

    private PerformanceAnalyticsDto.WorkerPerformance createWorker(String name, int collections, String efficiency, double rating) {
        PerformanceAnalyticsDto.WorkerPerformance w = new PerformanceAnalyticsDto.WorkerPerformance();
        w.setName(name);
        w.setCollections(collections);
        w.setEfficiency(efficiency);
        w.setRating(rating);
        return w;
    }
}
