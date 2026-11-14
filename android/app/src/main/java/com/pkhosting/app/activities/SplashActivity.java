package com.pkhosting.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseUser;
import com.pkhosting.app.databinding.ActivitySplashBinding;

/**
 * Splash screen: shows the logo and tagline briefly, then routes the user
 * to the dashboard if already signed in, or to the login screen otherwise.
 */
public class SplashActivity extends AppCompatActivity {

    private static final long SPLASH_DELAY_MS = 1600;

    private ActivitySplashBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            FirebaseUser user = getCurrentUser();
            Intent intent = new Intent(this, user != null ? MainActivity.class : LoginActivity.class);
            startActivity(intent);
            finish(); // Remove splash from the back stack.
        }, SPLASH_DELAY_MS);
    }

    /** Small helper so the Firebase import stays in one place. */
    private FirebaseUser getCurrentUser() {
        return com.google.firebase.auth.FirebaseAuth.getInstance().getCurrentUser();
    }
}
