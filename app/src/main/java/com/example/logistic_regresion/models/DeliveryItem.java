package com.example.logistic_regresion.models;

public class DeliveryItem {
    private String productName;
    private String deliveryTime;
    private float rating;

    public DeliveryItem(String productName, String deliveryTime, float rating) {
        this.productName = productName;
        this.deliveryTime = deliveryTime;
        this.rating = rating;
    }

    public String getProductName() {
        return productName;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public float getRating() {
        return rating;
    }
}