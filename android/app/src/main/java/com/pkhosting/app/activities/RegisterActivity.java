package com.pkhosting.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.pkhosting.app.R;
import com.pkhosting.app.databinding.ActivityRegisterBinding;
import com.pkhosting.app.models.AppUser;
import com.pkhosting.app.utils.Constants;
import com.pkhosting.app.utils.Validators;

/**
 * Registration screen: creates the Firebase Auth account and writes the
 * user profile document to the "users" collection.
 */
public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        binding.btnRegister.setOnClickListener(v -> attemptRegister());
        binding.textGoLogin.setOnClickListener(v -> finish());
    }

    private void attemptRegister() {
        String name = binding.editName.getText().toString().trim();
        String email = binding.editEmail.getText().toString().trim();
        String phone = binding.editPhone.getText().toString().trim();
        String password = binding.editPassword.getText().toString();
        String confirm = binding.editConfirmPassword.getText().toString();

        // Validate every field before touching the network.
        boolean valid = true;
        if (!Validators.isNonEmpty(name)) {
            binding.inputName.setError(getString(R.string.error_required));
            valid = false;
        } else binding.inputName.setError(null);

        if (!Validators.isValidEmail(email)) {
            binding.inputEmail.setError(getString(R.string.error_invalid_email));
            valid = false;
        } else binding.inputEmail.setError(null);

        if (!Validators.isValidPhone(phone)) {
            binding.inputPhone.setError(getString(R.string.error_invalid_phone));
            valid = false;
        } else binding.inputPhone.setError(null);

        if (!Validators.isValidPassword(password)) {
            binding.inputPassword.setError(getString(R.string.error_short_password));
            valid = false;
        } else binding.inputPassword.setError(null);

        if (!password.equals(confirm)) {
            binding.inputConfirmPassword.setError(getString(R.string.error_password_mismatch));
            valid = false;
        } else binding.inputConfirmPassword.setError(null);

        if (!valid) return;

        setLoading(true);
        auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    String uid = auth.getCurrentUser().getUid();
                    AppUser user = new AppUser(uid, name, email, phone, System.currentTimeMillis());
                    db.collection(Constants.COL_USERS).document(uid).set(user)
                            .addOnCompleteListener(task -> {
                                setLoading(false);
                                goToMain();
                            });
                })
                .addOnFailureListener(e -> {
                    setLoading(false);
                    Toast.makeText(this, getString(R.string.error_register_failed, e.getMessage()),
                            Toast.LENGTH_LONG).show();
                });
    }

    private void setLoading(boolean loading) {
        binding.progressRegister.setVisibility(loading ? View.VISIBLE : View.GONE);
        binding.btnRegister.setEnabled(!loading);
        binding.btnRegister.setText(loading ? R.string.creating_account : R.string.action_register);
    }

    private void goToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
