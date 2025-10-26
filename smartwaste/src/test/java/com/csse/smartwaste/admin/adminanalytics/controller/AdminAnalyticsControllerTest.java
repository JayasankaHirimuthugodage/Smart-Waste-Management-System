package com.csse.smartwaste.admin.adminanalytics.controller;

import com.csse.smartwaste.admin.adminanalytics.dto.*;
import com.csse.smartwaste.admin.adminanalytics.service.AdminAnalyticsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminAnalyticsController.class)
class AdminAnalyticsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminAnalyticsService analyticsService;

    @Test
    void shouldReturnPerformanceAnalytics() throws Exception {
        PerformanceAnalyticsDto dto = new PerformanceAnalyticsDto();
        dto.setCompletionRate(95.0);
        when(analyticsService.getPerformanceAnalytics()).thenReturn(dto);

        mockMvc.perform(get("/api/admin/analytics/performance"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.completionRate").value(95.0));
    }

    @Test
    void shouldReturnRecyclingAnalytics() throws Exception {
        RecyclingAnalyticsDto dto = new RecyclingAnalyticsDto();
        dto.setOverallRecyclingRate(70.0);
        when(analyticsService.getRecyclingAnalytics()).thenReturn(dto);

        mockMvc.perform(get("/api/admin/analytics/recycling"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.overallRecyclingRate").value(70.0));
    }

    @Test
    void shouldReturnFinancialAnalytics() throws Exception {
        FinancialAnalyticsDto dto = new FinancialAnalyticsDto();
        dto.setNetProfit(12345.0);
        when(analyticsService.getFinancialAnalytics()).thenReturn(dto);

        mockMvc.perform(get("/api/admin/analytics/financial"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.netProfit").value(12345.0));
    }

    @Test
    void shouldReturnEnvironmentalAnalytics() throws Exception {
        EnvironmentalAnalyticsDto dto = new EnvironmentalAnalyticsDto();
        dto.setCarbonSaved(12.4);
        dto.setGoals(List.of());
        dto.setImpactByArea(List.of());
        when(analyticsService.getEnvironmentalAnalytics()).thenReturn(dto);

        mockMvc.perform(get("/api/admin/analytics/environmental"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.carbonSaved").value(12.4));
    }

    @Test
    void shouldGenerateEnvironmentalReportPdf() throws Exception {
        EnvironmentalAnalyticsDto dto = new EnvironmentalAnalyticsDto();
        dto.setCarbonSaved(10);
        dto.setWasteDiverted(500);
        dto.setWaterSaved(200);
        dto.setTreesSaved(20);
        dto.setGoals(List.of());
        dto.setImpactByArea(List.of());
        when(analyticsService.getEnvironmentalAnalytics()).thenReturn(dto);

        mockMvc.perform(get("/api/admin/analytics/environmental/report"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/pdf"));
    }
}
