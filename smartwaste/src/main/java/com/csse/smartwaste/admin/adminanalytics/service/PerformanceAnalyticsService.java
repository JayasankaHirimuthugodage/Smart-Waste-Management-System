package com.csse.smartwaste.admin.adminanalytics.service;

import com.csse.smartwaste.admin.adminanalytics.dto.PerformanceAnalyticsDto;

/**
 * PerformanceAnalyticsService
 *
 * SRP: Defines contract for generating performance analytics data. OCP:
 * Extendable for new KPIs (e.g., vehicle utilization). DIP: High-level modules
 * depend on this abstraction, not implementation.
 */
public interface PerformanceAnalyticsService {

    /**
     * Generates performance analytics including route, worker, and collection
     * efficiency insights.
     *
     * @return PerformanceAnalyticsDto containing performance KPIs.
     */
    PerformanceAnalyticsDto generatePerformanceAnalytics();
}
