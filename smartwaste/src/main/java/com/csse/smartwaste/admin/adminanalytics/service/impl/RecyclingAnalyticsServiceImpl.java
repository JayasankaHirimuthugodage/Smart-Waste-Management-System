package com.csse.smartwaste.admin.adminanalytics.service.impl;

import com.csse.smartwaste.admin.adminanalytics.dto.RecyclingAnalyticsDto;
import com.csse.smartwaste.admin.adminanalytics.service.RecyclingAnalyticsService;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Uses mock IoT bin sensor data for material composition.
 */
@Service
public class RecyclingAnalyticsServiceImpl implements RecyclingAnalyticsService {

    @Override
    public RecyclingAnalyticsDto generateRecyclingAnalytics() {
        RecyclingAnalyticsDto dto = new RecyclingAnalyticsDto();
        dto.setOverallRecyclingRate(68);
        dto.setContaminationRate(8.5);
        dto.setParticipationRate(76);

        dto.setComposition(List.of(
                material("Paper & Cardboard", 32, 912),
                material("Plastics", 24, 684),
                material("Glass", 18, 513),
                material("Metals", 14, 399),
                material("Organic", 12, 342)
        ));

        dto.setAreaRates(List.of(
                area("Downtown", 72, "up", "+5%"),
                area("North Residential", 68, "up", "+3%"),
                area("South Residential", 65, "stable", "0%"),
                area("Industrial Zone", 58, "down", "-2%"),
                area("West Zone", 61, "up", "+4%")
        ));

        return dto;
    }

    private RecyclingAnalyticsDto.MaterialBreakdown material(String type, double perc, double weight) {
        RecyclingAnalyticsDto.MaterialBreakdown mb = new RecyclingAnalyticsDto.MaterialBreakdown();
        mb.setMaterial(type);
        mb.setPercentage(perc);
        mb.setWeightKg(weight);
        return mb;
    }

    private RecyclingAnalyticsDto.AreaRate area(String area, double rate, String trend, String change) {
        RecyclingAnalyticsDto.AreaRate ar = new RecyclingAnalyticsDto.AreaRate();
        ar.setArea(area);
        ar.setRate(rate);
        ar.setTrend(trend);
        ar.setChange(change);
        return ar;
    }
}
