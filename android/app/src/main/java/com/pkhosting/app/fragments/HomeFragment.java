package com.pkhosting.app.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.firestore.FirebaseFirestore;
import com.pkhosting.app.activities.MainActivity;
import com.pkhosting.app.activities.PlanDetailActivity;
import com.pkhosting.app.adapters.PlanAdapter;
import com.pkhosting.app.databinding.FragmentHomeBinding;
import com.pkhosting.app.models.Plan;
import com.pkhosting.app.utils.Constants;
import com.pkhosting.app.utils.Validators;

import java.util.ArrayList;
import java.util.List;

/**
 * Home dashboard: hero banner, domain search bar, featured plans and
 * quick action shortcuts.
 */
public class HomeFragment extends Fragment {

    public static final String TAG = "HomeFragment";

    private FragmentHomeBinding binding;
    private PlanAdapter featuredAdapter;
    private FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        db = FirebaseFirestore.getInstance();

        // Domain search bar at the top of the dashboard.
        binding.btnSearchDomain.setOnClickListener(v -> {
            String query = binding.editDomainQuery.getText().toString().trim().toLowerCase();
            if (!Validators.isValidDomainName(query)) {
                binding.inputDomainQuery.setError(getString(com.pkhosting.app.R.string.error_domain_name));
                return;
            }
            binding.inputDomainQuery.setError(null);
            ((MainActivity) requireActivity()).openDomains(query, false);
        });

        // Featured plans carousel.
        featuredAdapter = new PlanAdapter(new ArrayList<>(), true, this::openPlanDetail);
        binding.recyclerFeatured.setLayoutManager(new LinearLayoutManager(requireContext(),
                LinearLayoutManager.HORIZONTAL, false));
        binding.recyclerFeatured.setAdapter(featuredAdapter);
        loadFeaturedPlans();

        // Quick action cards.
        binding.cardActionRegister.setOnClickListener(v ->
                ((MainActivity) requireActivity()).openDomains("", false));
        binding.cardActionTransfer.setOnClickListener(v ->
                ((MainActivity) requireActivity()).openDomains("", true));
        binding.cardActionInvoices.setOnClickListener(v ->
                ((MainActivity) requireActivity()).showFragment(
                        new InvoicesFragment(), InvoicesFragment.TAG));
        binding.cardActionSupport.setOnClickListener(v ->
                ((MainActivity) requireActivity()).selectTab(com.pkhosting.app.R.id.nav_support));

        // Promo banner CTA.
        binding.btnViewPlans.setOnClickListener(v ->
                ((MainActivity) requireActivity()).selectTab(com.pkhosting.app.R.id.nav_plans));

        return binding.getRoot();
    }

    /** Loads the plans marked as popular; falls back to the first two plans. */
    private void loadFeaturedPlans() {
        binding.progressFeatured.setVisibility(View.VISIBLE);
        db.collection(Constants.COL_PLANS).whereEqualTo("popular", true).limit(2).get()
                .addOnSuccessListener(snapshot -> {
                    List<Plan> plans = toPlans(snapshot);
                    if (plans.isEmpty()) {
                        db.collection(Constants.COL_PLANS).limit(2).get()
                                .addOnSuccessListener(s -> showFeatured(toPlans(s)));
                    } else {
                        showFeatured(plans);
                    }
                })
                .addOnFailureListener(e -> {
                    binding.progressFeatured.setVisibility(View.GONE);
                    binding.textFeaturedEmpty.setVisibility(View.VISIBLE);
                });
    }

    private void showFeatured(List<Plan> plans) {
        binding.progressFeatured.setVisibility(View.GONE);
        if (plans.isEmpty()) {
            binding.textFeaturedEmpty.setVisibility(View.VISIBLE);
        } else {
            binding.textFeaturedEmpty.setVisibility(View.GONE);
            featuredAdapter.replace(plans);
        }
    }

    private List<Plan> toPlans(com.google.firebase.firestore.QuerySnapshot snapshot) {
        List<Plan> plans = new ArrayList<>();
        for (com.google.firebase.firestore.DocumentSnapshot doc : snapshot.getDocuments()) {
            Plan plan = doc.toObject(Plan.class);
            if (plan != null) {
                plan.setId(doc.getId());
                plans.add(plan);
            }
        }
        return plans;
    }

    private void openPlanDetail(Plan plan) {
        Intent intent = new Intent(requireContext(), PlanDetailActivity.class);
        intent.putExtra(PlanDetailActivity.EXTRA_PLAN_ID, plan.getId());
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
