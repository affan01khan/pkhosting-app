package com.pkhosting.app.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.pkhosting.app.R;
import com.pkhosting.app.databinding.ActivityOrderPaymentBinding;
import com.pkhosting.app.models.DomainItem;
import com.pkhosting.app.models.Invoice;
import com.pkhosting.app.models.Order;
import com.pkhosting.app.utils.Constants;
import com.pkhosting.app.utils.CurrencyUtil;
import com.pkhosting.app.utils.DateUtils;
import com.pkhosting.app.utils.OrderUtils;
import com.pkhosting.app.utils.PaymentManager;
import com.pkhosting.app.utils.Validators;

/**
 * Demo checkout screen. Collects a payment method, simulates the payment
 * with PaymentManager (Demo Mode) and then writes the Order, Invoice and,
 * for domain registrations, the Domain document to Firestore.
 *
 * The screen is intentionally structured so a real gateway can replace
 * PaymentManager later without touching the UI code.
 */
public class OrderPaymentActivity extends AppCompatActivity {

    public static final String EXTRA_TYPE = "type";
    public static final String EXTRA_ITEM_NAME = "itemName";
    public static final String EXTRA_DETAILS = "details";
    public static final String EXTRA_AMOUNT = "amount";
    public static final String EXTRA_DOMAIN_NAME = "domainName";
    public static final String EXTRA_YEARS = "years";

    private ActivityOrderPaymentBinding binding;
    private String type;
    private String itemName;
    private String details;
    private long amount;
    private String domainName;
    private long years;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderPaymentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.toolbar.setNavigationOnClickListener(v -> finish());

        type = getIntent().getStringExtra(EXTRA_TYPE);
        itemName = getIntent().getStringExtra(EXTRA_ITEM_NAME);
        details = getIntent().getStringExtra(EXTRA_DETAILS);
        amount = getIntent().getLongExtra(EXTRA_AMOUNT, 0);
        domainName = getIntent().getStringExtra(EXTRA_DOMAIN_NAME);
        years = getIntent().getLongExtra(EXTRA_YEARS, 1);

        binding.textItemName.setText(itemName);
        binding.textItemDetails.setText(details);
        binding.textAmount.setText(CurrencyUtil.format(amount));

        // Show the card fields only when the card method is selected.
        binding.radioCard.setOnCheckedChangeListener((button, checked) ->
                binding.layoutCardFields.setVisibility(checked ? View.VISIBLE : View.GONE));

        binding.btnPay.setOnClickListener(v -> attemptPayment());
    }

    private String selectedMethod() {
        int id = binding.radioGroupMethods.getCheckedRadioButtonId();
        if (id == R.id.radioJazzcash) return Constants.PAYMENT_METHODS[0];
        if (id == R.id.radioEasypaisa) return Constants.PAYMENT_METHODS[1];
        if (id == R.id.radioBank) return Constants.PAYMENT_METHODS[2];
        if (id == R.id.radioCard) return Constants.PAYMENT_METHODS[3];
        return null;
    }

    private void attemptPayment() {
        String method = selectedMethod();
        if (method == null) {
            Toast.makeText(this, R.string.error_select_method, Toast.LENGTH_SHORT).show();
            return;
        }

        // Card details are validated only for the (demo) card method.
        if (binding.radioCard.isChecked()) {
            boolean valid = true;
            String number = binding.editCardNumber.getText().toString().trim();
            String expiry = binding.editCardExpiry.getText().toString().trim();
            String cvv = binding.editCardCvv.getText().toString().trim();
            if (!Validators.isValidCardNumber(number)) {
                binding.inputCardNumber.setError(getString(R.string.error_card_number));
                valid = false;
            } else binding.inputCardNumber.setError(null);
            if (!Validators.isValidCardExpiry(expiry)) {
                binding.inputCardExpiry.setError(getString(R.string.error_card_expiry));
                valid = false;
            } else binding.inputCardExpiry.setError(null);
            if (!Validators.isValidCardCvv(cvv)) {
                binding.inputCardCvv.setError(getString(R.string.error_card_cvv));
                valid = false;
            } else binding.inputCardCvv.setError(null);
            if (!valid) return;
        }

        setLoading(true);
        PaymentManager.processDemoPayment(method, amount, (success, txnIdOrError) -> {
            if (!success) {
                setLoading(false);
                Toast.makeText(this, txnIdOrError, Toast.LENGTH_LONG).show();
                return;
            }
            saveOrder(method, txnIdOrError);
        });
    }

    private void saveOrder(String method, String txnId) {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String fullDetails = details + " · Txn " + txnId;

        // Domain registrations also create a document in "domains".
        DomainItem domain = null;
        if (Order.TYPE_DOMAIN.equals(type) && domainName != null && !domainName.isEmpty()) {
            long now = System.currentTimeMillis();
            domain = new DomainItem();
            domain.setDomainName(domainName);
            domain.setStatus(Order.STATUS_ACTIVE);
            domain.setYears(years);
            domain.setAmount(amount);
            domain.setRegisteredAt(now);
            domain.setExpiryAt(DateUtils.plusYears(now, years));
            domain.setAutoRenew(false);
        }

        OrderUtils.placeOrder(FirebaseFirestore.getInstance(), uid, type, itemName, fullDetails,
                amount, Order.STATUS_ACTIVE, Invoice.STATUS_PAID, method, domain,
                (orderId, invoiceId, error) -> {
                    setLoading(false);
                    if (error != null) {
                        Toast.makeText(this, getString(R.string.error_order_failed, error),
                                Toast.LENGTH_LONG).show();
                        return;
                    }
                    showSuccess(txnId);
                });
    }

    private void showSuccess(String txnId) {
        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.payment_success_title)
                .setMessage(getString(R.string.payment_success_message, txnId))
                .setPositiveButton(R.string.action_view_orders, (d, w) -> finish())
                .setCancelable(false)
                .show();
    }

    private void setLoading(boolean loading) {
        binding.progressPayment.setVisibility(loading ? View.VISIBLE : View.GONE);
        binding.btnPay.setEnabled(!loading);
        binding.btnPay.setText(loading ? R.string.processing_payment : R.string.action_pay_now);
    }
}
