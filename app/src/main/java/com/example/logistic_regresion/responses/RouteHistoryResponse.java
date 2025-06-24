package com.example.logistic_regresion.responses;

import com.example.logistic_regresion.requests.ReviewRequest;

public class RouteHistoryResponse {
    private String origin;
    private String destination;
    private String completionTime;
    private double payment;
    private String status; // Campo para el estado
    private double distance; // Campo para la distancia
    private ReviewRequest review;

    // Getters y setters
    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getCompletionTime() {
        return completionTime;
    }

    public void setCompletionTime(String completionTime) {
        this.completionTime = completionTime;
    }

    public double getPayment() {
        return payment;
    }

    public void setPayment(double payment) {
        this.payment = payment;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public ReviewRequest getReview() {
        return review;
    }

    public void setReview(ReviewRequest review) {
        this.review = review;
    }
}