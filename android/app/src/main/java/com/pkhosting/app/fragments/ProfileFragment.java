package com.pkhosting.app.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.pkhosting.app.R;
import com.pkhosting.app.activities.EditProfileActivity;
import com.pkhosting.app.databinding.FragmentProfileBinding;
import com.pkhosting.app.models.AppUser;
import com.pkhosting.app.utils.AuthUtils;
import com.pkhosting.app.utils.Constants;
import com.pkhosting.app.utils.DateUtils;

/**
 * Profile screen: shows the user's details with edit and logout actions.
 */
public class ProfileFragment extends Fragment {

    public static final String TAG = "ProfileFragment";

    private FragmentProfileBinding binding;
    private AppUser user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);

        binding.btnEditProfile.setOnClickListener(v ->
                startActivity(new Intent(requireContext(), EditProfileActivity.class)));
        binding.btnLogout.setOnClickListener(v -> confirmLogout());
        binding.btnRetry.setOnClickListener(v -> loadProfile());

        loadProfile();
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadProfile();
    }

    private void loadProfile() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        StateViews.showLoading(binding.progressList, binding.layoutContent,
                binding.layoutEmpty, binding.layoutError);
        FirebaseFirestore.getInstance().collection(Constants.COL_USERS).document(uid).get()
                .addOnSuccessListener(doc -> {
                    user = doc.toObject(AppUser.class);
                    if (user == null) {
                        binding.textEmptyTitle.setText(R.string.empty_profile_title);
                        binding.textEmptyMessage.setText(R.string.empty_profile_message);
                        binding.btnEmptyAction.setVisibility(View.GONE);
                        StateViews.showEmpty(binding.progressList, binding.layoutContent,
                                binding.layoutEmpty, binding.layoutError);
                        return;
                    }
                    showProfile();
                })
                .addOnFailureListener(e -> StateViews.showError(binding.progressList,
                        binding.layoutContent, binding.layoutEmpty, binding.layoutError));
    }

    private void showProfile() {
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        binding.textProfileName.setText(user.getName().isEmpty() ? email : user.getName());
        binding.textProfileEmail.setText(email);
        binding.textProfilePhone.setText(
                user.getPhone().isEmpty() ? getString(R.string.profile_no_phone) : user.getPhone());
        binding.textProfileMember.setText(user.getCreatedAt() == 0
                ? "-" : DateUtils.formatDate(user.getCreatedAt()));
        StateViews.showContent(binding.progressList, binding.layoutContent,
                binding.layoutEmpty, binding.layoutError);
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
