package com.csse.smartwaste.admin.routechanges.model;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "route_changes")
public class RouteChange {

    @Id
    private String id;
    private String routeName;
    private String area;
    private List<String> currentStops;
    private List<String> proposedStops;
    private double currentDistanceKm;
    private double proposedDistanceKm;
    private double wasteVolumePerDay;
    private String reason;
    private String optimizationType;
    private String priority;
    private String requestedBy;
    private String status;
    private LocalDateTime dateRequested;
    private LocalDateTime effectiveFrom;

    // ✅ Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public List<String> getCurrentStops() {
        return currentStops;
    }

    public void setCurrentStops(List<String> currentStops) {
        this.currentStops = currentStops;
    }

    public List<String> getProposedStops() {
        return proposedStops;
    }

    public void setProposedStops(List<String> proposedStops) {
        this.proposedStops = proposedStops;
    }

    public double getCurrentDistanceKm() {
        return currentDistanceKm;
    }

    public void setCurrentDistanceKm(double currentDistanceKm) {
        this.currentDistanceKm = currentDistanceKm;
    }

    public double getProposedDistanceKm() {
        return proposedDistanceKm;
    }

    public void setProposedDistanceKm(double proposedDistanceKm) {
        this.proposedDistanceKm = proposedDistanceKm;
    }

    public double getWasteVolumePerDay() {
        return wasteVolumePerDay;
    }

    public void setWasteVolumePerDay(double wasteVolumePerDay) {
        this.wasteVolumePerDay = wasteVolumePerDay;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getOptimizationType() {
        return optimizationType;
    }

    public void setOptimizationType(String optimizationType) {
        this.optimizationType = optimizationType;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getRequestedBy() {
        return requestedBy;
    }

    public void setRequestedBy(String requestedBy) {
        this.requestedBy = requestedBy;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getDateRequested() {
        return dateRequested;
    }

    public void setDateRequested(LocalDateTime dateRequested) {
        this.dateRequested = dateRequested;
    }

    public LocalDateTime getEffectiveFrom() {
        return effectiveFrom;
    }

    public void setEffectiveFrom(LocalDateTime effectiveFrom) {
        this.effectiveFrom = effectiveFrom;
    }
}
