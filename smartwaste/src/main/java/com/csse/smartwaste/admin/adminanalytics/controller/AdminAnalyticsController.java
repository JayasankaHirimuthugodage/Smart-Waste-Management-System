package com.csse.smartwaste.admin.adminanalytics.controller;

import java.io.ByteArrayOutputStream;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity; // NEW
import org.springframework.web.bind.annotation.CrossOrigin; // NEW
import org.springframework.web.bind.annotation.GetMapping; // NEW
import org.springframework.web.bind.annotation.RequestMapping; // NEW
import org.springframework.web.bind.annotation.RestController;

import com.csse.smartwaste.admin.adminanalytics.dto.EnvironmentalAnalyticsDto;
import com.csse.smartwaste.admin.adminanalytics.dto.FinancialAnalyticsDto;
import com.csse.smartwaste.admin.adminanalytics.dto.PerformanceAnalyticsDto;
import com.csse.smartwaste.admin.adminanalytics.dto.RecyclingAnalyticsDto;
import com.csse.smartwaste.admin.adminanalytics.service.AdminAnalyticsService;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * SRP: Exposes REST endpoints for all analytics dashboards.
 */
@RestController
@RequestMapping("/api/admin/analytics")
@CrossOrigin(origins = "*")
public class AdminAnalyticsController {

    private final AdminAnalyticsService analyticsService;

    public AdminAnalyticsController(AdminAnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/performance")
    public ResponseEntity<PerformanceAnalyticsDto> getPerformance() {
        return ResponseEntity.ok(analyticsService.getPerformanceAnalytics());
    }

    @GetMapping("/recycling")
    public ResponseEntity<RecyclingAnalyticsDto> getRecycling() {
        return ResponseEntity.ok(analyticsService.getRecyclingAnalytics());
    }

    @GetMapping("/financial")
    public ResponseEntity<FinancialAnalyticsDto> getFinancial() {
        return ResponseEntity.ok(analyticsService.getFinancialAnalytics());
    }

    @GetMapping("/environmental")
    public ResponseEntity<EnvironmentalAnalyticsDto> getEnvironmental() {
        return ResponseEntity.ok(analyticsService.getEnvironmentalAnalytics());
    }

    //  NEW — Generate downloadable environmental report (PDF)
    @GetMapping("/environmental/report")
    public ResponseEntity<byte[]> downloadEnvironmentalReport() {
        try {
            EnvironmentalAnalyticsDto dto = analyticsService.getEnvironmentalAnalytics();

            //  Create PDF
            Document document = new Document();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, out);
            document.open();

            //  Title
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, BaseColor.DARK_GRAY);
            document.add(new Paragraph("Environmental Impact Report", titleFont));
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Generated Report of Sustainability Metrics"));
            document.add(new Paragraph(" "));

            //  Summary Table
            PdfPTable summary = new PdfPTable(2);
            summary.addCell("CO₂ Saved (tons)");
            summary.addCell(String.valueOf(dto.getCarbonSaved()));
            summary.addCell("Waste Diverted (kg)");
            summary.addCell(String.valueOf(dto.getWasteDiverted()));
            summary.addCell("Water Saved (L)");
            summary.addCell(String.valueOf(dto.getWaterSaved()));
            summary.addCell("Trees Saved");
            summary.addCell(String.valueOf(dto.getTreesSaved()));
            document.add(summary);

            document.add(new Paragraph(" "));
            document.add(new Paragraph("Sustainability Goals:"));

            //  Goals Table
            PdfPTable goals = new PdfPTable(3);
            goals.addCell("Goal");
            goals.addCell("Progress (%)");
            goals.addCell("Target (%)");
            for (EnvironmentalAnalyticsDto.SustainabilityGoal g : dto.getGoals()) {
                goals.addCell(g.getGoal());
                goals.addCell(String.valueOf(g.getProgress()));
                goals.addCell(String.valueOf(g.getTarget()));
            }
            document.add(goals);

            document.add(new Paragraph(" "));
            document.add(new Paragraph("Impact by Area:"));

            //  Area Table
            PdfPTable areaTable = new PdfPTable(4);
            areaTable.addCell("Area");
            areaTable.addCell("CO₂ Saved");
            areaTable.addCell("Recycling Rate (%)");
            areaTable.addCell("Waste Reduced (kg)");
            for (EnvironmentalAnalyticsDto.AreaImpact a : dto.getImpactByArea()) {
                areaTable.addCell(a.getArea());
                areaTable.addCell(String.valueOf(a.getCarbonSaved()));
                areaTable.addCell(String.valueOf(a.getRecyclingRate()));
                areaTable.addCell(String.valueOf(a.getWasteReduced()));
            }
            document.add(areaTable);

            document.close();

            //  Return response
            byte[] pdfBytes = out.toByteArray();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "EnvironmentalImpactReport.pdf");
            headers.setContentLength(pdfBytes.length);

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("Error generating report: " + e.getMessage()).getBytes());
        }
    }
}
