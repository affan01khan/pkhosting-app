package com.pkhosting.app.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pkhosting.app.R;
import com.pkhosting.app.databinding.ItemDomainBinding;
import com.pkhosting.app.models.DomainItem;
import com.pkhosting.app.utils.DateUtils;

import java.util.List;

/**
 * Registered domains list (dashboard "My domains" section).
 */
public class DomainAdapter extends RecyclerView.Adapter<DomainAdapter.DomainHolder> {

    private final List<DomainItem> domains;

    public DomainAdapter(List<DomainItem> domains) {
        this.domains = domains;
    }

    public void replace(List<DomainItem> newDomains) {
        domains.clear();
        domains.addAll(newDomains);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DomainHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemDomainBinding binding = ItemDomainBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new DomainHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DomainHolder holder, int position) {
        holder.bind(domains.get(position));
    }

    @Override
    public int getItemCount() {
        return domains.size();
    }

    class DomainHolder extends RecyclerView.ViewHolder {
        private final ItemDomainBinding binding;

        DomainHolder(ItemDomainBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(DomainItem domain) {
            binding.textDomainName.setText(domain.getDomainName());
            binding.textDomainExpiry.setText(binding.getRoot().getContext()
                    .getString(R.string.domain_expires, DateUtils.formatDate(domain.getExpiryAt())));
            binding.chipDomainStatus.setText(domain.getStatus());
            binding.textAutoRenew.setText(domain.isAutoRenew()
                    ? R.string.auto_renew_on : R.string.auto_renew_off);
        }
    }
}
