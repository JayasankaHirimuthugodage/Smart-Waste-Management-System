package com.csse.smartwaste.admin.adminanalytics.service;

import org.springframework.stereotype.Service;

import com.csse.smartwaste.admin.adminanalytics.dto.EnvironmentalAnalyticsDto;
import com.csse.smartwaste.admin.adminanalytics.dto.FinancialAnalyticsDto;
import com.csse.smartwaste.admin.adminanalytics.dto.PerformanceAnalyticsDto;
import com.csse.smartwaste.admin.adminanalytics.dto.RecyclingAnalyticsDto;

/**
 * FACADE LAYER: Delegates analytics requests to domain-specific services. SRP:
 * Only coordinates analytics flow. DIP: Depends on abstractions (interfaces).
 */
@Service
public class AdminAnalyticsServiceImpl implements AdminAnalyticsService {

    private final FinancialAnalyticsService financialService;
    private final PerformanceAnalyticsService performanceService;
    private final RecyclingAnalyticsService recyclingService;
    private final EnvironmentalAnalyticsService environmentalService;

    public AdminAnalyticsServiceImpl(
            FinancialAnalyticsService financialService,
            PerformanceAnalyticsService performanceService,
            RecyclingAnalyticsService recyclingService,
            EnvironmentalAnalyticsService environmentalService) {
        this.financialService = financialService;
        this.performanceService = performanceService;
        this.recyclingService = recyclingService;
        this.environmentalService = environmentalService;
    }

    @Override
    public PerformanceAnalyticsDto getPerformanceAnalytics() {
        return performanceService.generatePerformanceAnalytics();
    }

    @Override
    public RecyclingAnalyticsDto getRecyclingAnalytics() {
        return recyclingService.generateRecyclingAnalytics();
    }

    @Override
    public FinancialAnalyticsDto getFinancialAnalytics() {
        return financialService.generateFinancialAnalytics();
    }

    @Override
    public EnvironmentalAnalyticsDto getEnvironmentalAnalytics() {
        return environmentalService.generateEnvironmentalAnalytics();
    }
}
