package com.example.logistic_regresion.responses;

import java.util.List;

public class SignupResponse {
    private Long id;
    private String username;
    private String email;
    private String password;
    private String verificationCode;
    private String verificationCodeExpiresAt;
    private boolean enabled;
    private String passwordResetToken;
    private String passwordResetTokenExpiresAt;
    private boolean accountNonLocked;
    private List<String> authorities;
    private boolean accountNonExpired;
    private boolean credentialsNonExpired;

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public String getVerificationCodeExpiresAt() {
        return verificationCodeExpiresAt;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String getPasswordResetToken() {
        return passwordResetToken;
    }

    public String getPasswordResetTokenExpiresAt() {
        return passwordResetTokenExpiresAt;
    }

    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    public List<String> getAuthorities() {
        return authorities;
    }

    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }
}