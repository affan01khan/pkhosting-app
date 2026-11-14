package com.pkhosting.app.utils;

import android.app.Activity;
import android.content.Intent;

import com.google.firebase.auth.FirebaseAuth;
import com.pkhosting.app.activities.LoginActivity;

/**
 * Shared authentication helpers.
 */
public final class AuthUtils {

    /** Signs the user out and restarts the flow at the login screen. */
    public static void logout(Activity activity) {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(activity, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
        activity.finish();
    }

    private AuthUtils() {
        // No instances.
    }
}
