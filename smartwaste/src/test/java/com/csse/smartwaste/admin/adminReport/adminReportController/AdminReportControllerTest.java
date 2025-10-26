package com.csse.smartwaste.admin.adminReport.adminReportController;

import com.csse.smartwaste.admin.adminReport.adminReportDto.ReportRequestDto;
import com.csse.smartwaste.admin.adminReport.adminReportDto.ReportSummaryDto;
import com.csse.smartwaste.admin.adminReport.adminReportServices.AdminReportService;
import com.csse.smartwaste.admin.adminReport.reportEntity.Report;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminReportController.class)
class AdminReportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminReportService adminReportService;

    private Report createMockReport() {
        Report report = new Report();
        report.setId("r1");
        report.setReportType("Waste Summary");
        report.setArea("Colombo");
        report.setDateRange("2025-01-01 to 2025-01-31");
        report.setSelectedWasteTypes(Map.of("Plastic", true, "Glass", false));
        report.setTotalWaste(2000);
        report.setRecyclingRate(75.0);
        return report;
    }

    private ReportSummaryDto createMockSummary() {
        ReportSummaryDto summary = new ReportSummaryDto();
        summary.setReportType("Waste Summary");
        summary.setArea("Colombo");
        summary.setTotalWaste(2000);
        summary.setRecyclingRate(75.0);
        summary.setHighWasteZones(List.of("Colombo", "Kandy"));
        return summary;
    }

    @Test
    void generateReport_ShouldReturnReportSummary() throws Exception {
        ReportSummaryDto summary = createMockSummary();
        when(adminReportService.generateReport(any(ReportRequestDto.class))).thenReturn(summary);

        String requestJson = """
                {
                  "reportType": "Waste Summary",
                  "area": "Colombo",
                  "dateRange": "2025-01-01 to 2025-01-31",
                  "selectedWasteTypes": {
                    "Plastic": true,
                    "Glass": false
                  }
                }
                """;

        mockMvc.perform(post("/api/admin/reports/generate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reportType").value("Waste Summary"))
                .andExpect(jsonPath("$.area").value("Colombo"))
                .andExpect(jsonPath("$.recyclingRate").value(75.0));
    }

    @Test
    void getAllReports_ShouldReturnListOfReports() throws Exception {
        when(adminReportService.getAllReports()).thenReturn(List.of(createMockReport()));

        mockMvc.perform(get("/api/admin/reports/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("r1"))
                .andExpect(jsonPath("$[0].reportType").value("Waste Summary"));
    }

    @Test
    void getReportById_ShouldReturnReport_WhenFound() throws Exception {
        when(adminReportService.getReportById("r1")).thenReturn(Optional.of(createMockReport()));

        mockMvc.perform(get("/api/admin/reports/r1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("r1"))
                .andExpect(jsonPath("$.area").value("Colombo"));
    }

    @Test
    void getReportById_ShouldReturn404_WhenNotFound() throws Exception {
        when(adminReportService.getReportById("notfound")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/admin/reports/notfound"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteReport_ShouldReturnSuccess_WhenDeleted() throws Exception {
        when(adminReportService.deleteReport("r1")).thenReturn(true);

        mockMvc.perform(delete("/api/admin/reports/r1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Report deleted successfully"))
                .andExpect(jsonPath("$.reportId").value("r1"));
    }

    @Test
    void deleteReport_ShouldReturn404_WhenNotFound() throws Exception {
        when(adminReportService.deleteReport("missing")).thenReturn(false);

        mockMvc.perform(delete("/api/admin/reports/missing"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Report not found"));
    }

    @Test
    void downloadReportPdf_ShouldReturnPdf_WhenReportExists() throws Exception {
        Report report = createMockReport();
        when(adminReportService.getReportById("r1")).thenReturn(Optional.of(report));

        mockMvc.perform(get("/api/admin/reports/pdf/r1"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", "attachment; filename=report-r1.pdf"))
                .andExpect(content().contentType(MediaType.APPLICATION_PDF));
    }

    @Test
    void downloadReportPdf_ShouldReturn404_WhenNotFound() throws Exception {
        when(adminReportService.getReportById("notfound")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/admin/reports/pdf/notfound"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Report Not Found"));
    }

    @Test
    void downloadReportPdf_ShouldReturn500_WhenPdfGenerationFails() throws Exception {
        // Simulate that the service itself fails when trying to fetch report
        when(adminReportService.getReportById("r2"))
                .thenThrow(new RuntimeException("PDF generation failed"));

        mockMvc.perform(get("/api/admin/reports/pdf/r2"))
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath("$.error").value("Unexpected error occurred"));
    }
}
