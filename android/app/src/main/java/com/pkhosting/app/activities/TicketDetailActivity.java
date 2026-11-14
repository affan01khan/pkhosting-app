package com.pkhosting.app.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.pkhosting.app.R;
import com.pkhosting.app.adapters.TicketReplyAdapter;
import com.pkhosting.app.databinding.ActivityTicketDetailBinding;
import com.pkhosting.app.models.Ticket;
import com.pkhosting.app.models.TicketReply;
import com.pkhosting.app.utils.Constants;
import com.pkhosting.app.utils.Validators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Conversation view for one support ticket: original message plus the
 * replies sub-collection, with a box to add another reply.
 */
public class TicketDetailActivity extends AppCompatActivity {

    public static final String EXTRA_TICKET_ID = "ticketId";

    private ActivityTicketDetailBinding binding;
    private FirebaseFirestore db;
    private TicketReplyAdapter adapter;
    private Ticket ticket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTicketDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        db = FirebaseFirestore.getInstance();

        binding.toolbar.setNavigationOnClickListener(v -> finish());

        adapter = new TicketReplyAdapter(new ArrayList<>());
        binding.recyclerReplies.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerReplies.setAdapter(adapter);

        String ticketId = getIntent().getStringExtra(EXTRA_TICKET_ID);
        if (ticketId == null || ticketId.isEmpty()) {
            finish();
            return;
        }
        loadTicket(ticketId);
        loadReplies(ticketId);

        binding.btnSendReply.setOnClickListener(v -> sendReply(ticketId));
    }

    private void loadTicket(String ticketId) {
        db.collection(Constants.COL_TICKETS).document(ticketId).get()
                .addOnSuccessListener(doc -> {
                    ticket = doc.toObject(Ticket.class);
                    if (ticket == null) return;
                    ticket.setId(doc.getId());
                    binding.textTicketSubject.setText(ticket.getSubject());
                    binding.textTicketMessage.setText(ticket.getMessage());
                    binding.chipTicketStatus.setText(ticket.getStatus());
                    binding.textTicketCategory.setText(ticket.getCategory());
                })
                .addOnFailureListener(e -> Toast.makeText(this,
                        getString(R.string.error_load_failed, e.getMessage()), Toast.LENGTH_LONG).show());
    }

    private void loadReplies(String ticketId) {
        binding.progressReplies.setVisibility(View.VISIBLE);
        db.collection(Constants.COL_TICKETS).document(ticketId)
                .collection(Constants.COL_REPLIES)
                .orderBy("createdAt", Query.Direction.ASCENDING)
                .get()
                .addOnSuccessListener(snapshot -> {
                    binding.progressReplies.setVisibility(View.GONE);
                    List<TicketReply> replies = new ArrayList<>();
                    for (DocumentSnapshot doc : snapshot.getDocuments()) {
                        replies.add(doc.toObject(TicketReply.class));
                    }
                    adapter.replace(replies);
                    binding.textRepliesEmpty.setVisibility(
                            replies.isEmpty() ? View.VISIBLE : View.GONE);
                })
                .addOnFailureListener(e -> {
                    binding.progressReplies.setVisibility(View.GONE);
                    Toast.makeText(this,
                            getString(R.string.error_load_failed, e.getMessage()),
                            Toast.LENGTH_LONG).show();
                });
    }

    private void sendReply(String ticketId) {
        String message = binding.editReply.getText().toString().trim();
        if (!Validators.isNonEmpty(message)) {
            binding.inputReply.setError(getString(R.string.error_required));
            return;
        }
        binding.inputReply.setError(null);
        binding.btnSendReply.setEnabled(false);

        String author = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        Map<String, Object> reply = new HashMap<>();
        reply.put("author", author == null ? "You" : author);
        reply.put("message", message);
        reply.put("createdAt", System.currentTimeMillis());

        db.collection(Constants.COL_TICKETS).document(ticketId)
                .collection(Constants.COL_REPLIES).add(reply)
                .addOnSuccessListener(ref -> {
                    binding.editReply.setText("");
                    binding.btnSendReply.setEnabled(true);
                    loadReplies(ticketId);
                })
                .addOnFailureListener(e -> {
                    binding.btnSendReply.setEnabled(true);
                    Toast.makeText(this,
                            getString(R.string.error_reply_failed, e.getMessage()),
                            Toast.LENGTH_LONG).show();
                });
    }
}
