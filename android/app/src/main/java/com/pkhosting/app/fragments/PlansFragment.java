package com.pkhosting.app.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.pkhosting.app.R;
import com.pkhosting.app.activities.MainActivity;
import com.pkhosting.app.adapters.PlanAdapter;
import com.pkhosting.app.databinding.FragmentPlansBinding;
import com.pkhosting.app.models.Plan;
import com.pkhosting.app.utils.Constants;
import com.pkhosting.app.utils.FirestoreSeeder;
import com.pkhosting.app.utils.StateViews;

import java.util.ArrayList;
import java.util.List;

/**
 * Hosting plans screen: all plans from Firestore with category filter
 * chips and a button that opens the comparison table.
 */
public class PlansFragment extends Fragment {

    public static final String TAG = "PlansFragment";

    private FragmentPlansBinding binding;
    private FirebaseFirestore db;
    private PlanAdapter adapter;
    private final List<Plan> allPlans = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentPlansBinding.inflate(inflater, container, false);
        db = FirebaseFirestore.getInstance();

        adapter = new PlanAdapter(new ArrayList<>(), false, plan -> {
            Intent intent = new android.content.Intent(requireContext(),
                    com.pkhosting.app.activities.PlanDetailActivity.class);
            intent.putExtra(com.pkhosting.app.activities.PlanDetailActivity.EXTRA_PLAN_ID, plan.getId());
            startActivity(intent);
        });
        binding.recyclerPlans.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerPlans.setAdapter(adapter);

        binding.chipGroupCategory.setOnCheckedStateChangeListener((group, checkedIds) -> applyFilter());
        binding.btnCompare.setOnClickListener(v ->
                ((MainActivity) requireActivity()).openPlanComparison());
        binding.listStates.btnRetry.setOnClickListener(v -> loadPlans());
        binding.listStates.btnEmptyAction.setOnClickListener(v -> seedSampleData());

        loadPlans();
        return binding.getRoot();
    }

    private void loadPlans() {
        StateViews.showLoading(binding.listStates.progressList, binding.recyclerPlans,
                binding.listStates.layoutEmpty, binding.listStates.layoutError);
        db.collection(Constants.COL_PLANS).get()
                .addOnSuccessListener(snapshot -> {
                    allPlans.clear();
                    for (DocumentSnapshot doc : snapshot.getDocuments()) {
                        Plan plan = doc.toObject(Plan.class);
                        if (plan != null) {
                            plan.setId(doc.getId());
                            allPlans.add(plan);
                        }
                    }
                    applyFilter();
                })
                .addOnFailureListener(e -> StateViews.showError(binding.listStates.progressList,
                        binding.recyclerPlans, binding.listStates.layoutEmpty, binding.listStates.layoutError));
    }

    /** Filters the loaded plans according to the selected chip. */
    private void applyFilter() {
        int checkedId = binding.chipGroupCategory.getCheckedChipId();
        String category = null;
        if (checkedId == R.id.chipShared) category = "Shared Hosting";
        if (checkedId == R.id.chipReseller) category = "Reseller Hosting";

        List<Plan> filtered = new ArrayList<>();
        for (Plan plan : allPlans) {
            if (category == null || category.equals(plan.getCategory())) {
                filtered.add(plan);
            }
        }

        if (filtered.isEmpty() && allPlans.isEmpty()) {
            // Database has no plans at all: offer the one-tap sample seeder.
            binding.listStates.textEmptyTitle.setText(R.string.empty_plans_title);
            binding.listStates.textEmptyMessage.setText(R.string.empty_plans_message);
            binding.listStates.btnEmptyAction.setVisibility(View.VISIBLE);
            StateViews.showEmpty(binding.listStates.progressList, binding.recyclerPlans,
                    binding.listStates.layoutEmpty, binding.listStates.layoutError);
        } else if (filtered.isEmpty()) {
            binding.listStates.textEmptyTitle.setText(R.string.empty_filter_title);
            binding.listStates.textEmptyMessage.setText(R.string.empty_filter_message);
            binding.listStates.btnEmptyAction.setVisibility(View.GONE);
            StateViews.showEmpty(binding.listStates.progressList, binding.recyclerPlans,
                    binding.listStates.layoutEmpty, binding.listStates.layoutError);
        } else {
            adapter.replace(filtered);
            StateViews.showContent(binding.listStates.progressList, binding.recyclerPlans,
                    binding.listStates.layoutEmpty, binding.listStates.layoutError);
        }
    }

    private void seedSampleData() {
        binding.listStates.progressList.setVisibility(View.VISIBLE);
        FirestoreSeeder.seedAll(db, (count, error) -> {
            if (!isAdded()) return;
            if (error != null) {
                Toast.makeText(requireContext(),
                        getString(R.string.error_seed_failed, error), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(requireContext(),
                        getString(R.string.seed_done, count), Toast.LENGTH_SHORT).show();
            }
            loadPlans();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
