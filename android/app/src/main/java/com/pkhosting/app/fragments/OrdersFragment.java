package com.pkhosting.app.fragments;

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
import com.pkhosting.app.adapters.OrderAdapter;
import com.pkhosting.app.databinding.FragmentOrdersBinding;
import com.pkhosting.app.models.Order;
import com.pkhosting.app.utils.Constants;
import com.pkhosting.app.utils.StateViews;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Order history screen: every order of the signed-in user, newest first.
 */
public class OrdersFragment extends Fragment {

    public static final String TAG = "OrdersFragment";

    private FragmentOrdersBinding binding;
    private OrderAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentOrdersBinding.inflate(inflater, container, false);

        adapter = new OrderAdapter(new ArrayList<>(), order -> { /* list only */ });
        binding.recyclerOrders.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerOrders.setAdapter(adapter);

        binding.listStates.btnRetry.setOnClickListener(v -> loadOrders());
        binding.listStates.btnEmptyAction.setOnClickListener(v -> loadOrders());
        binding.swipeRefresh.setOnRefreshListener(this::loadOrders);

        loadOrders();
        return binding.getRoot();
    }

    private void loadOrders() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        binding.swipeRefresh.setRefreshing(false);
        StateViews.showLoading(binding.listStates.progressList, binding.recyclerOrders,
                binding.listStates.layoutEmpty, binding.listStates.layoutError);

        FirebaseFirestore.getInstance().collection(Constants.COL_ORDERS)
                .whereEqualTo("userId", uid).get()
                .addOnSuccessListener(snapshot -> {
                    List<Order> orders = new ArrayList<>();
                    for (DocumentSnapshot doc : snapshot.getDocuments()) {
                        Order order = doc.toObject(Order.class);
                        if (order != null) {
                            order.setId(doc.getId());
                            orders.add(order);
                        }
                    }
                    Collections.sort(orders, (a, b) -> Long.compare(b.getCreatedAt(), a.getCreatedAt()));
                    adapter.replace(orders);

                    if (orders.isEmpty()) {
                        binding.listStates.textEmptyTitle.setText(R.string.empty_orders_title);
                        binding.listStates.textEmptyMessage.setText(R.string.empty_orders_message);
                        binding.listStates.btnEmptyAction.setVisibility(View.GONE);
                        StateViews.showEmpty(binding.listStates.progressList, binding.recyclerOrders,
                                binding.listStates.layoutEmpty, binding.listStates.layoutError);
                    } else {
                        StateViews.showContent(binding.listStates.progressList, binding.recyclerOrders,
                                binding.listStates.layoutEmpty, binding.listStates.layoutError);
                    }
                })
                .addOnFailureListener(e -> StateViews.showError(binding.listStates.progressList,
                        binding.recyclerOrders, binding.listStates.layoutEmpty, binding.listStates.layoutError));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
