package com.example.logistic_regresion.requests;

public class ResendVerificationRequest {
    private String email;
    private String codeType;

    public ResendVerificationRequest(String email, String codeType) {
        this.email = email;
        this.codeType = codeType;
    }

    public String getEmail() {
        return email;
    }

    public String getCodeType() {
        return codeType;
    }
}