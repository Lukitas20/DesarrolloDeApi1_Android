package com.example.logistic_regresion.repositories;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class TokenRepository {
    private static final String TAG = "TokenRepository";
    private static final String PREFS_NAME = "secure_prefs";
    private static final String KEY_AUTH_TOKEN = "AUTH_TOKEN";

    private final Context applicationContext;

    @Inject
    public TokenRepository(Context context) {
        this.applicationContext = context.getApplicationContext();
    }

    public void saveToken(String token) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            saveTokenModern(token);
        } else {
            saveTokenLegacy(token);
        }
    }

    public String getToken() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return getTokenModern();
        } else {
            return getTokenLegacy();
        }
    }

    public boolean hasToken() {
        return getToken() != null;
    }

    public void clearToken() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            clearTokenModern();
        } else {
            clearTokenLegacy();
        }
    }

    private void saveTokenModern(String token) {
        try {
            MasterKey masterKey = new MasterKey.Builder(applicationContext)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build();

            SharedPreferences encryptedPreferences = EncryptedSharedPreferences.create(
                    applicationContext,
                    PREFS_NAME,
                    masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
            encryptedPreferences.edit().putString(KEY_AUTH_TOKEN, token).apply();
            Log.d(TAG, "Token saved successfully (modern)");
        } catch (Exception e) {
            Log.e(TAG, "Error saving token (modern)", e);
        }
    }

    private void saveTokenLegacy(String token) {
        SharedPreferences sharedPreferences = applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(KEY_AUTH_TOKEN, token).apply();
    }

    private String getTokenModern() {
        try {
            MasterKey masterKey = new MasterKey.Builder(applicationContext)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build();

            SharedPreferences encryptedPreferences = EncryptedSharedPreferences.create(
                    applicationContext,
                    PREFS_NAME,
                    masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
            return encryptedPreferences.getString(KEY_AUTH_TOKEN, null);
        } catch (Exception e) {
            Log.e(TAG, "Error getting token (modern)", e);
            return null;
        }
    }

    private String getTokenLegacy() {
        SharedPreferences sharedPreferences = applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_AUTH_TOKEN, null);
    }

    private void clearTokenModern() {
        try {
            MasterKey masterKey = new MasterKey.Builder(applicationContext)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build();

            SharedPreferences encryptedPreferences = EncryptedSharedPreferences.create(
                    applicationContext,
                    PREFS_NAME,
                    masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
            encryptedPreferences.edit().remove(KEY_AUTH_TOKEN).apply();
        } catch (Exception e) {
            Log.e(TAG, "Error clearing token (modern)", e);
        }
    }
    public boolean isTokenValid() {
        String token = getToken();
        return token != null && !token.isEmpty();
    }
    private void clearTokenLegacy() {
        SharedPreferences sharedPreferences = applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        sharedPreferences.edit().remove(KEY_AUTH_TOKEN).apply();
    }
}