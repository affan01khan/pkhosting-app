package com.pkhosting.app.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pkhosting.app.databinding.ItemInvoiceBinding;
import com.pkhosting.app.models.Invoice;
import com.pkhosting.app.utils.CurrencyUtil;
import com.pkhosting.app.utils.DateUtils;

import java.util.List;

/**
 * Invoices list. Tapping an invoice opens the detail screen.
 */
public class InvoiceAdapter extends RecyclerView.Adapter<InvoiceAdapter.InvoiceHolder> {

    public interface OnInvoiceClickListener {
        void onInvoiceClick(Invoice invoice);
    }

    private final List<Invoice> invoices;
    private final OnInvoiceClickListener listener;

    public InvoiceAdapter(List<Invoice> invoices, OnInvoiceClickListener listener) {
        this.invoices = invoices;
        this.listener = listener;
    }

    public void replace(List<Invoice> newInvoices) {
        invoices.clear();
        invoices.addAll(newInvoices);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public InvoiceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemInvoiceBinding binding = ItemInvoiceBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new InvoiceHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull InvoiceHolder holder, int position) {
        holder.bind(invoices.get(position));
    }

    @Override
    public int getItemCount() {
        return invoices.size();
    }

    class InvoiceHolder extends RecyclerView.ViewHolder {
        private final ItemInvoiceBinding binding;

        InvoiceHolder(ItemInvoiceBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Invoice invoice) {
            binding.textInvoiceNumber.setText(invoice.getNumber());
            binding.textInvoiceDescription.setText(invoice.getDescription());
            binding.textInvoiceDate.setText(DateUtils.formatDate(invoice.getCreatedAt()));
            binding.textInvoiceAmount.setText(CurrencyUtil.format(invoice.getAmount()));
            binding.chipInvoiceStatus.setText(invoice.getStatus());
            binding.getRoot().setOnClickListener(v -> listener.onInvoiceClick(invoice));
        }
    }
}
