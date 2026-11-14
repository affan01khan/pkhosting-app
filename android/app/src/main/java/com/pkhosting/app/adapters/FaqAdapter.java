package com.pkhosting.app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pkhosting.app.databinding.ItemFaqBinding;
import com.pkhosting.app.models.Faq;

import java.util.List;

/**
 * Knowledgebase list. Tapping a question expands or collapses the answer.
 */
public class FaqAdapter extends RecyclerView.Adapter<FaqAdapter.FaqHolder> {

    private final List<Faq> faqs;

    public FaqAdapter(List<Faq> faqs) {
        this.faqs = faqs;
    }

    public void replace(List<Faq> newFaqs) {
        faqs.clear();
        faqs.addAll(newFaqs);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FaqHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFaqBinding binding = ItemFaqBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new FaqHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FaqHolder holder, int position) {
        holder.bind(faqs.get(position));
    }

    @Override
    public int getItemCount() {
        return faqs.size();
    }

    class FaqHolder extends RecyclerView.ViewHolder {
        private final ItemFaqBinding binding;

        FaqHolder(ItemFaqBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Faq faq) {
            binding.textFaqQuestion.setText(faq.getQuestion());
            binding.textFaqAnswer.setText(faq.getAnswer());
            binding.chipFaqCategory.setText(faq.getCategory());

            // Answers always start collapsed; tapping the card toggles them.
            binding.textFaqAnswer.setVisibility(View.GONE);

            binding.getRoot().setOnClickListener(v -> {
                boolean isVisible = binding.textFaqAnswer.getVisibility() == View.VISIBLE;
                binding.textFaqAnswer.setVisibility(isVisible ? View.GONE : View.VISIBLE);
            });
        }
    }
}
