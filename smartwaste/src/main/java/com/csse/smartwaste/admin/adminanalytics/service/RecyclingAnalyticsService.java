package com.csse.smartwaste.admin.adminanalytics.service;

import com.csse.smartwaste.admin.adminanalytics.dto.RecyclingAnalyticsDto;

/**
 * RecyclingAnalyticsService
 *
 * SRP: Defines contract for recycling analytics logic. OCP: Extendable for
 * future waste types (e.g., e-waste, organic compost). DIP: Higher-level
 * modules depend on this abstraction.
 */
public interface RecyclingAnalyticsService {

    /**
     * Generates recycling analytics including material composition,
     * contamination rate, participation levels, and area-based rates.
     *
     * @return RecyclingAnalyticsDto containing key recycling metrics.
     */
    RecyclingAnalyticsDto generateRecyclingAnalytics();
}
