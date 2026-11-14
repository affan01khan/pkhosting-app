package com.pkhosting.app.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pkhosting.app.databinding.ItemTicketReplyBinding;
import com.pkhosting.app.models.TicketReply;
import com.pkhosting.app.utils.DateUtils;

import java.util.List;

/**
 * Replies inside a ticket conversation.
 */
public class TicketReplyAdapter extends RecyclerView.Adapter<TicketReplyAdapter.ReplyHolder> {

    private final List<TicketReply> replies;

    public TicketReplyAdapter(List<TicketReply> replies) {
        this.replies = replies;
    }

    public void replace(List<TicketReply> newReplies) {
        replies.clear();
        replies.addAll(newReplies);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ReplyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTicketReplyBinding binding = ItemTicketReplyBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ReplyHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ReplyHolder holder, int position) {
        holder.bind(replies.get(position));
    }

    @Override
    public int getItemCount() {
        return replies.size();
    }

    class ReplyHolder extends RecyclerView.ViewHolder {
        private final ItemTicketReplyBinding binding;

        ReplyHolder(ItemTicketReplyBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(TicketReply reply) {
            binding.textReplyAuthor.setText(reply.getAuthor());
            binding.textReplyMessage.setText(reply.getMessage());
            binding.textReplyDate.setText(DateUtils.formatDate(reply.getCreatedAt()));
        }
    }
}
