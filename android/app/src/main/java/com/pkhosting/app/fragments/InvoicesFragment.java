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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.pkhosting.app.R;
import com.pkhosting.app.activities.InvoiceDetailActivity;
import com.pkhosting.app.adapters.InvoiceAdapter;
import com.pkhosting.app.databinding.FragmentInvoicesBinding;
import com.pkhosting.app.models.Invoice;
import com.pkhosting.app.utils.Constants;
import com.pkhosting.app.utils.StateViews;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Invoices list screen. Tapping an invoice opens its detail screen.
 */
public class InvoicesFragment extends Fragment {

    public static final String TAG = "InvoicesFragment";

    private FragmentInvoicesBinding binding;
    private InvoiceAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentInvoicesBinding.inflate(inflater, container, false);

        adapter = new InvoiceAdapter(new ArrayList<>(), invoice -> {
            Intent intent = new Intent(requireContext(), InvoiceDetailActivity.class);
            intent.putExtra(InvoiceDetailActivity.EXTRA_INVOICE_ID, invoice.getId());
            startActivity(intent);
        });
        binding.recyclerInvoices.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerInvoices.setAdapter(adapter);

        binding.listStates.btnRetry.setOnClickListener(v -> loadInvoices());
        binding.listStates.btnEmptyAction.setOnClickListener(v -> loadInvoices());
        binding.swipeRefresh.setOnRefreshListener(this::loadInvoices);

        loadInvoices();
        return binding.getRoot();
    }

    private void loadInvoices() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        binding.swipeRefresh.setRefreshing(false);
        StateViews.showLoading(binding.listStates.progressList, binding.recyclerInvoices,
                binding.listStates.layoutEmpty, binding.listStates.layoutError);

        FirebaseFirestore.getInstance().collection(Constants.COL_INVOICES)
                .whereEqualTo("userId", uid).get()
                .addOnSuccessListener(snapshot -> {
                    List<Invoice> invoices = new ArrayList<>();
                    for (DocumentSnapshot doc : snapshot.getDocuments()) {
                        Invoice invoice = doc.toObject(Invoice.class);
                        if (invoice != null) {
                            invoice.setId(doc.getId());
                            invoices.add(invoice);
                        }
                    }
                    Collections.sort(invoices, (a, b) -> Long.compare(b.getCreatedAt(), a.getCreatedAt()));
                    adapter.replace(invoices);

                    if (invoices.isEmpty()) {
                        binding.listStates.textEmptyTitle.setText(R.string.empty_invoices_title);
                        binding.listStates.textEmptyMessage.setText(R.string.empty_invoices_message);
                        binding.listStates.btnEmptyAction.setVisibility(View.GONE);
                        StateViews.showEmpty(binding.listStates.progressList, binding.recyclerInvoices,
                                binding.listStates.layoutEmpty, binding.listStates.layoutError);
                    } else {
                        StateViews.showContent(binding.listStates.progressList, binding.recyclerInvoices,
                                binding.listStates.layoutEmpty, binding.listStates.layoutError);
                    }
                })
                .addOnFailureListener(e -> StateViews.showError(binding.listStates.progressList,
                        binding.recyclerInvoices, binding.listStates.layoutEmpty, binding.listStates.layoutError));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
