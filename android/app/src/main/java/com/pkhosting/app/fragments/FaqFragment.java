package com.pkhosting.app.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.pkhosting.app.adapters.FaqAdapter;
import com.pkhosting.app.databinding.FragmentFaqBinding;
import com.pkhosting.app.models.Faq;
import com.pkhosting.app.utils.Constants;
import com.pkhosting.app.utils.FirestoreSeeder;
import com.pkhosting.app.utils.StateViews;

import java.util.ArrayList;
import java.util.List;

/**
 * FAQs / knowledgebase with live text search and category chips.
 */
public class FaqFragment extends Fragment {

    public static final String TAG = "FaqFragment";

    private FragmentFaqBinding binding;
    private FaqAdapter adapter;
    private final List<Faq> allFaqs = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentFaqBinding.inflate(inflater, container, false);

        adapter = new FaqAdapter(new ArrayList<>());
        binding.recyclerFaqs.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerFaqs.setAdapter(adapter);

        // Live search: every keystroke re-filters the loaded list.
        binding.editFaqSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int a, int b, int c) { }
            @Override public void onTextChanged(CharSequence s, int a, int b, int c) { }
            @Override public void afterTextChanged(Editable s) { applyFilter(); }
        });

        binding.chipGroupFaqCategory.setOnCheckedStateChangeListener((group, checkedIds) -> applyFilter());
        binding.listStates.btnRetry.setOnClickListener(v -> loadFaqs());
        binding.listStates.btnEmptyAction.setOnClickListener(v -> seedFaqs());

        loadFaqs();
        return binding.getRoot();
    }

    private void loadFaqs() {
        StateViews.showLoading(binding.listStates.progressList, binding.recyclerFaqs,
                binding.listStates.layoutEmpty, binding.listStates.layoutError);
        FirebaseFirestore.getInstance().collection(Constants.COL_FAQS).get()
                .addOnSuccessListener(snapshot -> {
                    allFaqs.clear();
                    for (DocumentSnapshot doc : snapshot.getDocuments()) {
                        Faq faq = doc.toObject(Faq.class);
                        if (faq != null) {
                            faq.setId(doc.getId());
                            allFaqs.add(faq);
                        }
                    }
                    applyFilter();
                })
                .addOnFailureListener(e -> StateViews.showError(binding.listStates.progressList,
                        binding.recyclerFaqs, binding.listStates.layoutEmpty, binding.listStates.layoutError));
    }

    /** Applies the search text and category chip filters. */
    private void applyFilter() {
        String query = binding.editFaqSearch.getText().toString().trim().toLowerCase();
        int checkedId = binding.chipGroupFaqCategory.getCheckedChipId();
        String category = null;
        if (checkedId == R.id.chipFaqHosting) category = "Hosting";
        if (checkedId == R.id.chipFaqBilling) category = "Billing";
        if (checkedId == R.id.chipFaqSupport) category = "Support";

        List<Faq> filtered = new ArrayList<>();
        for (Faq faq : allFaqs) {
            boolean matchesCategory = category == null || category.equals(faq.getCategory());
            boolean matchesQuery = query.isEmpty()
                    || faq.getQuestion().toLowerCase().contains(query)
                    || faq.getAnswer().toLowerCase().contains(query);
            if (matchesCategory && matchesQuery) {
                filtered.add(faq);
            }
        }

        if (filtered.isEmpty() && allFaqs.isEmpty()) {
            binding.listStates.textEmptyTitle.setText(R.string.empty_faqs_title);
            binding.listStates.textEmptyMessage.setText(R.string.empty_faqs_message);
            binding.listStates.btnEmptyAction.setVisibility(View.VISIBLE);
            StateViews.showEmpty(binding.listStates.progressList, binding.recyclerFaqs,
                    binding.listStates.layoutEmpty, binding.listStates.layoutError);
        } else if (filtered.isEmpty()) {
            binding.listStates.textEmptyTitle.setText(R.string.empty_search_title);
            binding.listStates.textEmptyMessage.setText(R.string.empty_search_message);
            binding.listStates.btnEmptyAction.setVisibility(View.GONE);
            StateViews.showEmpty(binding.listStates.progressList, binding.recyclerFaqs,
                    binding.listStates.layoutEmpty, binding.listStates.layoutError);
        } else {
            adapter.replace(filtered);
            StateViews.showContent(binding.listStates.progressList, binding.recyclerFaqs,
                    binding.listStates.layoutEmpty, binding.listStates.layoutError);
        }
    }

    private void seedFaqs() {
        binding.listStates.progressList.setVisibility(View.VISIBLE);
        FirestoreSeeder.seedFaqs(FirebaseFirestore.getInstance(), (count, error) -> {
            if (!isAdded()) return;
            if (error != null) {
                Toast.makeText(requireContext(),
                        getString(R.string.error_seed_failed, error), Toast.LENGTH_LONG).show();
            }
            loadFaqs();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
