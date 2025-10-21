package com.csse.smartwaste.admin.pickup.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "special_pickups")
public class SpecialPickup {

    @Id
    private String id;

    private String name;
    private String area;
    private String date;
    private String type;
    private String status; // pending, approved, declined

    public SpecialPickup() {
    }

    public SpecialPickup(String name, String area, String date, String type, String status) {
        this.name = name;
        this.area = area;
        this.date = date;
        this.type = type;
        this.status = status;
    }

    // Getters & Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
