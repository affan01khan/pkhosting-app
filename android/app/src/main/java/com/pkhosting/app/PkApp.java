package com.pkhosting.app;

import android.app.Application;
import androidx.appcompat.app.AppCompatDelegate;

/**
 * Application class. Runs once when the process starts.
 * The PKHosting palette is designed for light mode, so dark mode is
 * force-disabled to keep the brand colours consistent everywhere.
 */
public class PkApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }
}
