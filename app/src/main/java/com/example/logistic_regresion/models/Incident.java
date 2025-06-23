package com.example.logistic_regresion.models;

public class Incident {
    private Long id;
    private String type;
    private String description;
    private String photoUrl;
    private Long routeId;

    // Constructor for creating a new incident report
    public Incident(String type, String description, String photoUrl, Long routeId) {
        this.type = type;
        this.description = description;
        this.photoUrl = photoUrl;
        this.routeId = routeId;
    }

    // Default constructor needed for Retrofit
    public Incident() {
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public Long getRouteId() {
        return routeId;
    }

    public void setRouteId(Long routeId) {
        this.routeId = routeId;
    }
}
