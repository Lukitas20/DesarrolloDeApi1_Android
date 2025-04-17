package com.example.logistic_regresion.models;

public class Route {
    private String origin;
    private String destination;
    private double distance;

    // Constructor
    public Route(String origin, String destination, double distance) {
        this.origin = origin;
        this.destination = destination;
        this.distance = distance;
    }

    // Getters
    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    public double getDistance() {
        return distance;
    }
}