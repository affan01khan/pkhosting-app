package com.pkhosting.app.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.pkhosting.app.R;
import com.pkhosting.app.databinding.ActivityForgotPasswordBinding;
import com.pkhosting.app.utils.Validators;

/**
 * Forgot password screen: sends a Firebase password reset email.
 */
public class ForgotPasswordActivity extends AppCompatActivity {

    private ActivityForgotPasswordBinding binding;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgotPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();

        binding.btnSendReset.setOnClickListener(v -> sendResetEmail());
        binding.toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void sendResetEmail() {
        String email = binding.editEmail.getText().toString().trim();
        if (!Validators.isValidEmail(email)) {
            binding.inputEmail.setError(getString(R.string.error_invalid_email));
            return;
        }
        binding.inputEmail.setError(null);
        setLoading(true);
        auth.sendPasswordResetEmail(email)
                .addOnSuccessListener(unused -> {
                    setLoading(false);
                    Toast.makeText(this, R.string.reset_email_sent, Toast.LENGTH_LONG).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    setLoading(false);
                    Toast.makeText(this, getString(R.string.error_reset_failed, e.getMessage()),
                            Toast.LENGTH_LONG).show();
                });
    }

    private void setLoading(boolean loading) {
        binding.progressReset.setVisibility(loading ? View.VISIBLE : View.GONE);
        binding.btnSendReset.setEnabled(!loading);
    }
}
