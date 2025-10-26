package com.csse.smartwaste.admin.adminanalytics.service;

import com.csse.smartwaste.admin.adminanalytics.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class AdminAnalyticsServiceImplTest {

    private FinancialAnalyticsService financialService;
    private PerformanceAnalyticsService performanceService;
    private RecyclingAnalyticsService recyclingService;
    private EnvironmentalAnalyticsService environmentalService;
    private AdminAnalyticsServiceImpl analyticsService;

    @BeforeEach
    void setup() {
        financialService = Mockito.mock(FinancialAnalyticsService.class);
        performanceService = Mockito.mock(PerformanceAnalyticsService.class);
        recyclingService = Mockito.mock(RecyclingAnalyticsService.class);
        environmentalService = Mockito.mock(EnvironmentalAnalyticsService.class);

        analyticsService = new AdminAnalyticsServiceImpl(
                financialService, performanceService, recyclingService, environmentalService);
    }

    @Test
    void getPerformanceAnalytics_shouldReturnDataFromService() {
        PerformanceAnalyticsDto expected = new PerformanceAnalyticsDto();
        expected.setCompletionRate(90.0);
        when(performanceService.generatePerformanceAnalytics()).thenReturn(expected);

        PerformanceAnalyticsDto result = analyticsService.getPerformanceAnalytics();

        assertThat(result.getCompletionRate()).isEqualTo(90.0);
    }

    @Test
    void getRecyclingAnalytics_shouldReturnDataFromService() {
        RecyclingAnalyticsDto dto = new RecyclingAnalyticsDto();
        dto.setOverallRecyclingRate(65.0);
        when(recyclingService.generateRecyclingAnalytics()).thenReturn(dto);

        RecyclingAnalyticsDto result = analyticsService.getRecyclingAnalytics();

        assertThat(result.getOverallRecyclingRate()).isEqualTo(65.0);
    }

    @Test
    void getFinancialAnalytics_shouldReturnDataFromService() {
        FinancialAnalyticsDto dto = new FinancialAnalyticsDto();
        dto.setNetProfit(5000);
        when(financialService.generateFinancialAnalytics()).thenReturn(dto);

        FinancialAnalyticsDto result = analyticsService.getFinancialAnalytics();

        assertThat(result.getNetProfit()).isEqualTo(5000);
    }

    @Test
    void getEnvironmentalAnalytics_shouldReturnDataFromService() {
        EnvironmentalAnalyticsDto dto = new EnvironmentalAnalyticsDto();
        dto.setCarbonSaved(20);
        when(environmentalService.generateEnvironmentalAnalytics()).thenReturn(dto);

        EnvironmentalAnalyticsDto result = analyticsService.getEnvironmentalAnalytics();

        assertThat(result.getCarbonSaved()).isEqualTo(20);
    }
}
