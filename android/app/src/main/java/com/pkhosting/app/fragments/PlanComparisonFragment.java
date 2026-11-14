package com.pkhosting.app.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.pkhosting.app.databinding.FragmentPlanComparisonBinding;

/**
 * Side-by-side comparison of the three shared hosting plans.
 * The table content is static because it mirrors the published plan
 * limits on pkhosting.com.
 */
public class PlanComparisonFragment extends Fragment {

    private FragmentPlanComparisonBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentPlanComparisonBinding.inflate(inflater, container, false);
        binding.btnBack.setOnClickListener(v ->
                requireActivity().getOnBackPressedDispatcher().onBackPressed());
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
