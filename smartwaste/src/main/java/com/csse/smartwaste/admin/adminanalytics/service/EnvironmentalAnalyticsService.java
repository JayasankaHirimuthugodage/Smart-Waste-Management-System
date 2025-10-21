package com.csse.smartwaste.admin.adminanalytics.service;

import com.csse.smartwaste.admin.adminanalytics.dto.EnvironmentalAnalyticsDto;

/**
 * EnvironmentalAnalyticsService
 *
 * SRP: Defines contract for environmental and sustainability analytics. OCP:
 * Can be extended for new KPIs like renewable energy usage or emission
 * intensity. DIP: High-level modules depend on this abstraction.
 */
public interface EnvironmentalAnalyticsService {

    /**
     * Generates environmental analytics report including carbon saved, water
     * saved, trees saved, and sustainability goals progress.
     *
     * @return EnvironmentalAnalyticsDto containing sustainability KPIs.
     */
    EnvironmentalAnalyticsDto generateEnvironmentalAnalytics();
}
