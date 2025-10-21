package com.csse.smartwaste.admin.pickup.model;

public class SpecialPickup {

    private String id;
    private String name;
    private String area;
    private String date;
    private String type;
    private String status; // pending, approved, declined

    public SpecialPickup(String id, String name, String area, String date, String type, String status) {
        this.id = id;
        this.name = name;
        this.area = area;
        this.date = date;
        this.type = type;
        this.status = status;
    }

    public SpecialPickup() {
    }

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
