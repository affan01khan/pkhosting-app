package com.pkhosting.app.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.pkhosting.app.R;
import com.pkhosting.app.databinding.ActivityInvoiceDetailBinding;
import com.pkhosting.app.models.Invoice;
import com.pkhosting.app.models.Order;
import com.pkhosting.app.utils.Constants;
import com.pkhosting.app.utils.CurrencyUtil;
import com.pkhosting.app.utils.DateUtils;
import com.pkhosting.app.utils.PaymentManager;

/**
 * Invoice detail screen with a "Pay now (Demo)" button for unpaid invoices.
 */
public class InvoiceDetailActivity extends AppCompatActivity {

    public static final String EXTRA_INVOICE_ID = "invoiceId";

    private ActivityInvoiceDetailBinding binding;
    private FirebaseFirestore db;
    private Invoice invoice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInvoiceDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        db = FirebaseFirestore.getInstance();

        binding.toolbar.setNavigationOnClickListener(v -> finish());

        String invoiceId = getIntent().getStringExtra(EXTRA_INVOICE_ID);
        if (invoiceId == null || invoiceId.isEmpty()) {
            finish();
            return;
        }
        loadInvoice(invoiceId);
        binding.btnPayInvoice.setOnClickListener(v -> payInvoice());
    }

    private void loadInvoice(String invoiceId) {
        binding.progressInvoice.setVisibility(View.VISIBLE);
        binding.contentInvoice.setVisibility(View.GONE);
        db.collection(Constants.COL_INVOICES).document(invoiceId).get()
                .addOnSuccessListener(this::showInvoice)
                .addOnFailureListener(e -> {
                    binding.progressInvoice.setVisibility(View.GONE);
                    binding.textInvoiceError.setVisibility(View.VISIBLE);
                    binding.textInvoiceError.setText(getString(R.string.error_load_failed, e.getMessage()));
                });
    }

    private void showInvoice(DocumentSnapshot document) {
        binding.progressInvoice.setVisibility(View.GONE);
        invoice = document.toObject(Invoice.class);
        if (invoice == null) {
            binding.textInvoiceError.setVisibility(View.VISIBLE);
            binding.textInvoiceError.setText(R.string.error_invoice_not_found);
            return;
        }
        invoice.setId(document.getId());
        binding.contentInvoice.setVisibility(View.VISIBLE);

        binding.textInvoiceNumber.setText(invoice.getNumber());
        binding.textInvoiceDescription.setText(invoice.getDescription());
        binding.textInvoiceAmount.setText(CurrencyUtil.format(invoice.getAmount()));
        binding.textInvoiceIssued.setText(DateUtils.formatDate(invoice.getCreatedAt()));
        binding.textInvoiceDue.setText(DateUtils.formatDate(invoice.getDueAt()));
        binding.textInvoiceStatus.setText(invoice.getStatus());

        boolean unpaid = Invoice.STATUS_UNPAID.equals(invoice.getStatus());
        binding.btnPayInvoice.setVisibility(unpaid ? View.VISIBLE : View.GONE);
        binding.textStatusPaid.setVisibility(unpaid ? View.GONE : View.VISIBLE);
    }

    /** Demo payment for an unpaid invoice, then updates Firestore. */
    private void payInvoice() {
        if (invoice == null) return;
        setLoading(true);
        PaymentManager.processDemoPayment(Constants.PAYMENT_METHODS[3], invoice.getAmount(),
                (success, txnIdOrError) -> {
                    if (!success) {
                        setLoading(false);
                        Toast.makeText(this, txnIdOrError, Toast.LENGTH_LONG).show();
                        return;
                    }
                    markPaid(txnIdOrError);
                });
    }

    private void markPaid(String txnId) {
        invoice.setStatus(Invoice.STATUS_PAID);
        invoice.setPaymentMethod(Constants.PAYMENT_METHODS[3]);
        db.collection(Constants.COL_INVOICES).document(invoice.getId()).set(invoice)
                .addOnSuccessListener(unused -> {
                    // Unpaid invoices block their order; activate it once paid.
                    if (invoice.getOrderId() != null && !invoice.getOrderId().isEmpty()) {
                        db.collection(Constants.COL_ORDERS).document(invoice.getOrderId())
                                .update("status", Order.STATUS_ACTIVE);
                    }
                    setLoading(false);
                    new MaterialAlertDialogBuilder(this)
                            .setTitle(R.string.payment_success_title)
                            .setMessage(getString(R.string.payment_success_message, txnId))
                            .setPositiveButton(android.R.string.ok, (d, w) -> showInvoiceAgain())
                            .setCancelable(false)
                            .show();
                })
                .addOnFailureListener(e -> {
                    setLoading(false);
                    Toast.makeText(this, getString(R.string.error_update_failed, e.getMessage()),
                            Toast.LENGTH_LONG).show();
                });
    }

    private void showInvoiceAgain() {
        binding.btnPayInvoice.setVisibility(View.GONE);
        binding.textInvoiceStatus.setText(Invoice.STATUS_PAID);
        binding.textStatusPaid.setVisibility(View.VISIBLE);
    }

    private void setLoading(boolean loading) {
        binding.progressInvoice.setVisibility(loading ? View.VISIBLE : View.GONE);
        binding.btnPayInvoice.setEnabled(!loading);
    }
}
