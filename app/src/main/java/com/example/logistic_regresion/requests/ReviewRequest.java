package com.example.logistic_regresion.requests;

public class ReviewRequest {
    private Long routeId;
    private String comentario;
    private int puntuacion;
    private String imagenUrl;

    public String getComentario() { return comentario; }

    public int getPuntuacion() { return puntuacion; }

    public String getImagenUrl() { return imagenUrl; }

    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }
}
