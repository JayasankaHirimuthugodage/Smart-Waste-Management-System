package com.csse.smartwaste.admin.adminanalytics.service;

import com.csse.smartwaste.admin.adminanalytics.dto.FinancialAnalyticsDto;

/**
 * FinancialAnalyticsService
 *
 * SRP: Defines contract for generating financial analytics data. OCP: Can be
 * extended with new financial metrics without changing existing methods. DIP:
 * Higher-level modules depend on this abstraction, not concrete
 * implementations.
 */
public interface FinancialAnalyticsService {

    /**
     * Generates a complete financial analytics report including revenue, costs,
     * and recent transactions.
     *
     * @return FinancialAnalyticsDto containing all financial KPIs
     */
    FinancialAnalyticsDto generateFinancialAnalytics();
}
