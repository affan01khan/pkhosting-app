package com.pkhosting.app.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.pkhosting.app.R;
import com.pkhosting.app.databinding.ActivityEditProfileBinding;
import com.pkhosting.app.utils.Constants;
import com.pkhosting.app.utils.Validators;

import java.util.HashMap;
import java.util.Map;

/**
 * Edit the profile: updates the "users" document in Firestore.
 */
public class EditProfileActivity extends AppCompatActivity {

    private ActivityEditProfileBinding binding;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        db = FirebaseFirestore.getInstance();

        binding.toolbar.setNavigationOnClickListener(v -> finish());
        binding.btnSaveProfile.setOnClickListener(v -> saveProfile());
        loadProfile();
    }

    private void loadProfile() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        binding.progressProfile.setVisibility(View.VISIBLE);
        db.collection(Constants.COL_USERS).document(uid).get()
                .addOnSuccessListener(doc -> {
                    binding.progressProfile.setVisibility(View.GONE);
                    binding.editName.setText(doc.getString("name"));
                    binding.editEmail.setText(doc.getString("email"));
                    binding.editPhone.setText(doc.getString("phone"));
                })
                .addOnFailureListener(e -> {
                    binding.progressProfile.setVisibility(View.GONE);
                    Toast.makeText(this, getString(R.string.error_load_failed, e.getMessage()),
                            Toast.LENGTH_LONG).show();
                });
    }

    private void saveProfile() {
        String name = binding.editName.getText().toString().trim();
        String phone = binding.editPhone.getText().toString().trim();

        boolean valid = true;
        if (!Validators.isNonEmpty(name)) {
            binding.inputName.setError(getString(R.string.error_required));
            valid = false;
        } else binding.inputName.setError(null);
        if (!Validators.isValidPhone(phone)) {
            binding.inputPhone.setError(getString(R.string.error_invalid_phone));
            valid = false;
        } else binding.inputPhone.setError(null);
        if (!valid) return;

        setLoading(true);
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Map<String, Object> updates = new HashMap<>();
        updates.put("name", name);
        updates.put("phone", phone);

        db.collection(Constants.COL_USERS).document(uid).update(updates)
                .addOnSuccessListener(unused -> {
                    setLoading(false);
                    Toast.makeText(this, R.string.profile_updated, Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    setLoading(false);
                    Toast.makeText(this, getString(R.string.error_update_failed, e.getMessage()),
                            Toast.LENGTH_LONG).show();
                });
    }

    private void setLoading(boolean loading) {
        binding.progressProfile.setVisibility(loading ? View.VISIBLE : View.GONE);
        binding.btnSaveProfile.setEnabled(!loading);
    }
}
