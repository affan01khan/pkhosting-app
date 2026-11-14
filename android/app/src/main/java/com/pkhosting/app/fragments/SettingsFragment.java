package com.pkhosting.app.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.firestore.FirebaseFirestore;
import com.pkhosting.app.R;
import com.pkhosting.app.databinding.FragmentSettingsBinding;
import com.pkhosting.app.utils.AuthUtils;
import com.pkhosting.app.utils.FirestoreSeeder;

/**
 * Settings: notification preference (stored locally), sample data loader,
 * app information and logout.
 */
public class SettingsFragment extends Fragment {

    public static final String TAG = "SettingsFragment";

    private static final String PREFS = "pkhosting_prefs";
    private static final String KEY_NOTIFICATIONS = "notifications";

    private FragmentSettingsBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);

        SharedPreferences prefs = requireContext().getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        binding.switchNotifications.setChecked(prefs.getBoolean(KEY_NOTIFICATIONS, true));
        binding.switchNotifications.setOnCheckedChangeListener((button, checked) ->
                prefs.edit().putBoolean(KEY_NOTIFICATIONS, checked).apply());

        binding.btnSeedData.setOnClickListener(v -> seedSampleData());
        binding.btnLogout.setOnClickListener(v -> confirmLogout());

        binding.textAppVersion.setText(getString(R.string.settings_version, appVersion()));
        binding.textContactInfo.setText(getString(R.string.settings_contact,
                com.pkhosting.app.utils.Constants.SUPPORT_EMAIL,
                com.pkhosting.app.utils.Constants.SUPPORT_PHONE));

        return binding.getRoot();
    }

    private String appVersion() {
        try {
            return requireContext().getPackageManager()
                    .getPackageInfo(requireContext().getPackageName(), 0).versionName;
        } catch (Exception e) {
            return "1.0";
        }
    }

    private void seedSampleData() {
        binding.progressSeed.setVisibility(View.VISIBLE);
        FirestoreSeeder.seedAll(FirebaseFirestore.getInstance(), (count, error) -> {
            if (!isAdded()) return;
            binding.progressSeed.setVisibility(View.GONE);
            if (error != null) {
                Toast.makeText(requireContext(),
                        getString(R.string.error_seed_failed, error), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(requireContext(),
                        getString(R.string.seed_done, count), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void confirmLogout() {
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.logout_title)
                .setMessage(R.string.logout_message)
                .setPositiveButton(R.string.action_logout, (d, w) -> AuthUtils.logout(requireActivity()))
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
