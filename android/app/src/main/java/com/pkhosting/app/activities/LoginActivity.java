package com.pkhosting.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.pkhosting.app.R;
import com.pkhosting.app.databinding.ActivityLoginBinding;
import com.pkhosting.app.utils.Validators;

/**
 * Login screen (Firebase Authentication, email + password).
 */
public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();

        binding.btnLogin.setOnClickListener(v -> attemptLogin());
        binding.textGoRegister.setOnClickListener(v ->
                startActivity(new Intent(this, RegisterActivity.class)));
        binding.textForgotPassword.setOnClickListener(v ->
                startActivity(new Intent(this, ForgotPasswordActivity.class)));
    }

    private void attemptLogin() {
        String email = binding.editEmail.getText().toString().trim();
        String password = binding.editPassword.getText().toString();

        // Input validation with inline error messages.
        boolean valid = true;
        if (!Validators.isValidEmail(email)) {
            binding.inputEmail.setError(getString(R.string.error_invalid_email));
            valid = false;
        } else {
            binding.inputEmail.setError(null);
        }
        if (!Validators.isValidPassword(password)) {
            binding.inputPassword.setError(getString(R.string.error_short_password));
            valid = false;
        } else {
            binding.inputPassword.setError(null);
        }
        if (!valid) return;

        setLoading(true);
        auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    setLoading(false);
                    goToMain();
                })
                .addOnFailureListener(e -> {
                    setLoading(false);
                    Toast.makeText(this, getString(R.string.error_login_failed, e.getMessage()),
                            Toast.LENGTH_LONG).show();
                });
    }

    private void setLoading(boolean loading) {
        binding.progressLogin.setVisibility(loading ? View.VISIBLE : View.GONE);
        binding.btnLogin.setEnabled(!loading);
        binding.btnLogin.setText(loading ? R.string.logging_in : R.string.action_login);
    }

    private void goToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
