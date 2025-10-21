package com.csse.smartwaste.admin.adminanalytics.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.csse.smartwaste.admin.adminanalytics.dto.EnvironmentalAnalyticsDto;
import com.csse.smartwaste.admin.adminanalytics.service.EnvironmentalAnalyticsService;

/**
 * Computes sustainability KPIs using mock IoT and recycling data.
 */
@Service
public class EnvironmentalAnalyticsServiceImpl implements EnvironmentalAnalyticsService {

    @Override
    public EnvironmentalAnalyticsDto generateEnvironmentalAnalytics() {
        EnvironmentalAnalyticsDto dto = new EnvironmentalAnalyticsDto();
        dto.setCarbonSaved(12.4);
        dto.setWasteDiverted(1847);
        dto.setWaterSaved(4580);
        dto.setTreesSaved(156);

        dto.setGoals(List.of(
                goal("Carbon Neutrality by 2030", 35, 100),
                goal("80% Waste Diversion Rate", 68, 80),
                goal("Zero Landfill Operations", 45, 100),
                goal("Renewable Energy Usage", 52, 100)
        ));

        dto.setImpactByArea(List.of(
                area("Downtown", 3.2, 72, 485),
                area("North Residential", 2.8, 68, 412),
                area("South Residential", 2.5, 65, 389),
                area("Industrial Zone", 2.1, 58, 324),
                area("West Zone", 1.8, 61, 237)
        ));

        return dto;
    }

    private EnvironmentalAnalyticsDto.SustainabilityGoal goal(String g, double p, double t) {
        EnvironmentalAnalyticsDto.SustainabilityGoal goal = new EnvironmentalAnalyticsDto.SustainabilityGoal();
        goal.setGoal(g);
        goal.setProgress(p);
        goal.setTarget(t);
        return goal;
    }

    private EnvironmentalAnalyticsDto.AreaImpact area(String name, double c, double r, double w) {
        EnvironmentalAnalyticsDto.AreaImpact area = new EnvironmentalAnalyticsDto.AreaImpact();
        area.setArea(name);
        area.setCarbonSaved(c);
        area.setRecyclingRate(r);
        area.setWasteReduced(w);
        return area;
    }
}
