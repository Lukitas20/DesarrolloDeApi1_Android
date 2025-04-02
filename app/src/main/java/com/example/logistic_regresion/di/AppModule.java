package com.example.logistic_regresion.di;

import android.app.Application;
import android.content.Context;

import com.example.logistic_regresion.repositories.TokenRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class AppModule {

    @Provides
    @Singleton
    Context provideApplicationContext(Application application) {
        return application.getApplicationContext();
    }

    @Provides
    @Singleton
    TokenRepository provideTokenRepository(Context context) {
        return new TokenRepository(context);
    }
}