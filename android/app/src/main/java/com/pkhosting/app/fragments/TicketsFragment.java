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
import com.pkhosting.app.activities.MainActivity;
import com.pkhosting.app.activities.TicketDetailActivity;
import com.pkhosting.app.adapters.TicketAdapter;
import com.pkhosting.app.databinding.FragmentTicketsBinding;
import com.pkhosting.app.models.Ticket;
import com.pkhosting.app.utils.Constants;
import com.pkhosting.app.utils.StateViews;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Support tickets list with a floating "new ticket" button.
 */
public class TicketsFragment extends Fragment {

    public static final String TAG = "TicketsFragment";

    private FragmentTicketsBinding binding;
    private TicketAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentTicketsBinding.inflate(inflater, container, false);

        adapter = new TicketAdapter(new ArrayList<>(), ticket -> {
            Intent intent = new Intent(requireContext(), TicketDetailActivity.class);
            intent.putExtra(TicketDetailActivity.EXTRA_TICKET_ID, ticket.getId());
            startActivity(intent);
        });
        binding.recyclerTickets.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerTickets.setAdapter(adapter);

        binding.fabNewTicket.setOnClickListener(v ->
                ((MainActivity) requireActivity()).showFragment(
                        new NewTicketFragment(), NewTicketFragment.TAG));
        binding.listStates.btnRetry.setOnClickListener(v -> loadTickets());
        binding.listStates.btnEmptyAction.setOnClickListener(v ->
                ((MainActivity) requireActivity()).showFragment(
                        new NewTicketFragment(), NewTicketFragment.TAG));
        binding.swipeRefresh.setOnRefreshListener(this::loadTickets);

        loadTickets();
        return binding.getRoot();
    }

    private void loadTickets() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        binding.swipeRefresh.setRefreshing(false);
        StateViews.showLoading(binding.listStates.progressList, binding.recyclerTickets,
                binding.listStates.layoutEmpty, binding.listStates.layoutError);

        FirebaseFirestore.getInstance().collection(Constants.COL_TICKETS)
                .whereEqualTo("userId", uid).get()
                .addOnSuccessListener(snapshot -> {
                    List<Ticket> tickets = new ArrayList<>();
                    for (DocumentSnapshot doc : snapshot.getDocuments()) {
                        Ticket ticket = doc.toObject(Ticket.class);
                        if (ticket != null) {
                            ticket.setId(doc.getId());
                            tickets.add(ticket);
                        }
                    }
                    Collections.sort(tickets, (a, b) -> Long.compare(b.getCreatedAt(), a.getCreatedAt()));
                    adapter.replace(tickets);

                    if (tickets.isEmpty()) {
                        binding.listStates.textEmptyTitle.setText(R.string.empty_tickets_title);
                        binding.listStates.textEmptyMessage.setText(R.string.empty_tickets_message);
                        binding.listStates.btnEmptyAction.setVisibility(View.VISIBLE);
                        StateViews.showEmpty(binding.listStates.progressList, binding.recyclerTickets,
                                binding.listStates.layoutEmpty, binding.listStates.layoutError);
                    } else {
                        StateViews.showContent(binding.listStates.progressList, binding.recyclerTickets,
                                binding.listStates.layoutEmpty, binding.listStates.layoutError);
                    }
                })
                .addOnFailureListener(e -> StateViews.showError(binding.listStates.progressList,
                        binding.recyclerTickets, binding.listStates.layoutEmpty, binding.listStates.layoutError));
    }

    @Override
    public void onResume() {
        super.onResume();
        loadTickets();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
