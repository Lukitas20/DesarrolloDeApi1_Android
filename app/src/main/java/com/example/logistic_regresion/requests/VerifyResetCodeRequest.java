package com.example.logistic_regresion.requests;

public class VerifyResetCodeRequest {
    private String email;
    private String code;

    public VerifyResetCodeRequest(String email, String code) {
        this.email = email;
        this.code = code;
    }

    public String getEmail() {
        return email;
    }

    public String getCode() {
        return code;
    }
}