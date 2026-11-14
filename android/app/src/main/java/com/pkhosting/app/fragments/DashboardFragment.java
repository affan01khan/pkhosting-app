package com.pkhosting.app.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.pkhosting.app.R;
import com.pkhosting.app.activities.MainActivity;
import com.pkhosting.app.adapters.DomainAdapter;
import com.pkhosting.app.adapters.OrderAdapter;
import com.pkhosting.app.databinding.FragmentDashboardBinding;
import com.pkhosting.app.models.DomainItem;
import com.pkhosting.app.models.Invoice;
import com.pkhosting.app.models.Order;
import com.pkhosting.app.utils.Constants;
import com.pkhosting.app.utils.StateViews;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Customer dashboard: quick stats (active services, domains, unpaid
 * invoices), recent orders and registered domains.
 */
public class DashboardFragment extends Fragment {

    public static final String TAG = "DashboardFragment";

    private FragmentDashboardBinding binding;
    private FirebaseFirestore db;
    private OrderAdapter ordersAdapter;
    private DomainAdapter domainsAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        db = FirebaseFirestore.getInstance();

        ordersAdapter = new OrderAdapter(new ArrayList<>(), order -> { /* no detail screen */ });
        binding.recyclerServices.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerServices.setAdapter(ordersAdapter);
        binding.recyclerServices.setNestedScrollingEnabled(false);

        domainsAdapter = new DomainAdapter(new ArrayList<>());
        binding.recyclerDomains.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerDomains.setAdapter(domainsAdapter);
        binding.recyclerDomains.setNestedScrollingEnabled(false);

        binding.listStates.btnRetry.setOnClickListener(v -> loadDashboard());
        binding.swipeRefresh.setOnRefreshListener(this::loadDashboard);
        binding.cardStatDomains.setOnClickListener(v ->
                ((MainActivity) requireActivity()).showFragment(
                        new com.pkhosting.app.fragments.DomainSearchFragment(), DomainSearchFragment.TAG));

        loadDashboard();
        return binding.getRoot();
    }

    private void loadDashboard() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        binding.swipeRefresh.setRefreshing(false);
        StateViews.showLoading(binding.listStates.progressList, binding.layoutContent,
                binding.listStates.layoutEmpty, binding.listStates.layoutError);

        Task<QuerySnapshot> tOrders = db.collection(Constants.COL_ORDERS)
                .whereEqualTo("userId", uid).get();
        Task<QuerySnapshot> tDomains = db.collection(Constants.COL_DOMAINS)
                .whereEqualTo("userId", uid).get();
        Task<QuerySnapshot> tInvoices = db.collection(Constants.COL_INVOICES)
                .whereEqualTo("userId", uid).get();
        Task<DocumentSnapshot> tUser = db.collection(Constants.COL_USERS).document(uid).get();

        com.google.android.gms.tasks.Tasks.whenAllSuccess(tOrders, tDomains, tInvoices, tUser)
                .addOnSuccessListener(results -> {
                    QuerySnapshot ordersSnap = (QuerySnapshot) results.get(0);
                    QuerySnapshot domainsSnap = (QuerySnapshot) results.get(1);
                    QuerySnapshot invoicesSnap = (QuerySnapshot) results.get(2);
                    DocumentSnapshot userDoc = (DocumentSnapshot) results.get(3);

                    showGreeting(userDoc);
                    showOrders(ordersSnap);
                    showDomains(domainsSnap);
                    showStats(ordersSnap, domainsSnap, invoicesSnap);

                    StateViews.showContent(binding.listStates.progressList, binding.layoutContent,
                            binding.listStates.layoutEmpty, binding.listStates.layoutError);
                })
                .addOnFailureListener(e -> StateViews.showError(binding.listStates.progressList,
                        binding.layoutContent, binding.listStates.layoutEmpty, binding.listStates.layoutError));
    }

    private void showGreeting(DocumentSnapshot userDoc) {
        String name = userDoc.getString("name");
        binding.textGreeting.setText(getString(R.string.dashboard_greeting,
                name == null || name.isEmpty() ? getString(R.string.guest_user) : name));
    }

    private void showOrders(QuerySnapshot snapshot) {
        List<Order> orders = new ArrayList<>();
        for (DocumentSnapshot doc : snapshot.getDocuments()) {
            Order order = doc.toObject(Order.class);
            if (order != null) {
                order.setId(doc.getId());
                orders.add(order);
            }
        }
        // Newest first (sorted client-side so no composite index is needed).
        Collections.sort(orders, (a, b) -> Long.compare(b.getCreatedAt(), a.getCreatedAt()));

        int active = 0;
        for (Order o : orders) {
            if (Order.STATUS_ACTIVE.equals(o.getStatus())) active++;
        }
        binding.textStatServices.setText(String.valueOf(active));

        List<Order> recent = orders.subList(0, Math.min(3, orders.size()));
        ordersAdapter.replace(recent);
        binding.textServicesEmpty.setVisibility(
                recent.isEmpty() ? View.VISIBLE : View.GONE);
    }

    private void showDomains(QuerySnapshot snapshot) {
        List<DomainItem> domains = new ArrayList<>();
        for (DocumentSnapshot doc : snapshot.getDocuments()) {
            DomainItem domain = doc.toObject(DomainItem.class);
            if (domain != null) {
                domain.setId(doc.getId());
                domains.add(domain);
            }
        }
        Collections.sort(domains, (a, b) -> Long.compare(b.getRegisteredAt(), a.getRegisteredAt()));

        binding.textStatDomains.setText(String.valueOf(domains.size()));

        List<DomainItem> recent = domains.subList(0, Math.min(3, domains.size()));
        domainsAdapter.replace(recent);
        binding.textDomainsEmpty.setVisibility(
                recent.isEmpty() ? View.VISIBLE : View.GONE);
    }

    private void showStats(QuerySnapshot ordersSnap, QuerySnapshot domainsSnap,
                           QuerySnapshot invoicesSnap) {
        int unpaid = 0;
        for (DocumentSnapshot doc : invoicesSnap.getDocuments()) {
            if (Invoice.STATUS_UNPAID.equals(doc.getString("status"))) unpaid++;
        }
        binding.textStatInvoices.setText(String.valueOf(unpaid));
    }

    @Override
    public void onResume() {
        super.onResume();
        loadDashboard();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
