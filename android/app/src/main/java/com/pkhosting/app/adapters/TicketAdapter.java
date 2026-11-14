package com.pkhosting.app.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pkhosting.app.databinding.ItemTicketBinding;
import com.pkhosting.app.models.Ticket;
import com.pkhosting.app.utils.DateUtils;

import java.util.List;

/**
 * Support tickets list. Tapping a ticket opens the conversation screen.
 */
public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.TicketHolder> {

    public interface OnTicketClickListener {
        void onTicketClick(Ticket ticket);
    }

    private final List<Ticket> tickets;
    private final OnTicketClickListener listener;

    public TicketAdapter(List<Ticket> tickets, OnTicketClickListener listener) {
        this.tickets = tickets;
        this.listener = listener;
    }

    public void replace(List<Ticket> newTickets) {
        tickets.clear();
        tickets.addAll(newTickets);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TicketHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTicketBinding binding = ItemTicketBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new TicketHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TicketHolder holder, int position) {
        holder.bind(tickets.get(position));
    }

    @Override
    public int getItemCount() {
        return tickets.size();
    }

    class TicketHolder extends RecyclerView.ViewHolder {
        private final ItemTicketBinding binding;

        TicketHolder(ItemTicketBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Ticket ticket) {
            binding.textTicketSubject.setText(ticket.getSubject());
            binding.textTicketCategory.setText(ticket.getCategory());
            binding.textTicketDate.setText(DateUtils.formatDate(ticket.getCreatedAt()));
            binding.chipTicketStatus.setText(ticket.getStatus());
            binding.getRoot().setOnClickListener(v -> listener.onTicketClick(ticket));
        }
    }
}
