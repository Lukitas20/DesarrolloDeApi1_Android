package com.example.logistic_regresion.requests;

public class VerifyRequest {
    private String email;
    private String verificationCode;

    public VerifyRequest(String email, String verificationCode) {
        this.email = email;
        this.verificationCode = verificationCode;
    }

    public String getEmail() {
        return email;
    }

    public String getVerificationCode() {
        return verificationCode;
    }
}