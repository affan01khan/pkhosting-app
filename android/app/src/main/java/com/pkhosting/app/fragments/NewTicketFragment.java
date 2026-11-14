package com.pkhosting.app.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.pkhosting.app.R;
import com.pkhosting.app.databinding.FragmentNewTicketBinding;
import com.pkhosting.app.models.Ticket;
import com.pkhosting.app.utils.Constants;
import com.pkhosting.app.utils.Validators;

/**
 * New support ticket form. Writes a document to the "tickets" collection
 * with status "Open".
 */
public class NewTicketFragment extends Fragment {

    public static final String TAG = "NewTicketFragment";

    private FragmentNewTicketBinding binding;
    private FirebaseFirestore db;
    private String selectedCategory = "Technical";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentNewTicketBinding.inflate(inflater, container, false);
        db = FirebaseFirestore.getInstance();

        String[] categories = {"Technical", "Billing", "Domains", "Sales", "Other"};
        binding.dropdownCategory.setSimpleItems(categories);
        binding.dropdownCategory.setText(categories[0], false);
        binding.dropdownCategory.setOnItemClickListener((parent, view, position, id) ->
                selectedCategory = categories[position]);

        binding.btnSubmitTicket.setOnClickListener(v -> submitTicket());
        return binding.getRoot();
    }

    private void submitTicket() {
        String subject = binding.editSubject.getText().toString().trim();
        String message = binding.editMessage.getText().toString().trim();

        boolean valid = true;
        if (!Validators.isNonEmpty(subject)) {
            binding.inputSubject.setError(getString(R.string.error_required));
            valid = false;
        } else binding.inputSubject.setError(null);
        if (!Validators.isNonEmpty(message) || message.length() < 10) {
            binding.inputMessage.setError(getString(R.string.error_message_short));
            valid = false;
        } else binding.inputMessage.setError(null);
        if (!valid) return;

        setLoading(true);
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Ticket ticket = new Ticket();
        ticket.setUserId(uid);
        ticket.setSubject(subject);
        ticket.setCategory(selectedCategory);
        ticket.setMessage(message);
        ticket.setStatus(Ticket.STATUS_OPEN);
        ticket.setCreatedAt(System.currentTimeMillis());

        db.collection(Constants.COL_TICKETS).add(ticket)
                .addOnSuccessListener(ref -> {
                    if (!isAdded()) return;
                    setLoading(false);
                    Toast.makeText(requireContext(), R.string.ticket_created, Toast.LENGTH_LONG).show();
                    requireActivity().getOnBackPressedDispatcher().onBackPressed();
                })
                .addOnFailureListener(e -> {
                    if (!isAdded()) return;
                    setLoading(false);
                    Toast.makeText(requireContext(),
                            getString(R.string.error_ticket_failed, e.getMessage()),
                            Toast.LENGTH_LONG).show();
                });
    }

    private void setLoading(boolean loading) {
        binding.progressTicket.setVisibility(loading ? View.VISIBLE : View.GONE);
        binding.btnSubmitTicket.setEnabled(!loading);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
