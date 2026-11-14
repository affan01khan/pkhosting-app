package com.pkhosting.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.pkhosting.app.R;
import com.pkhosting.app.databinding.ActivityPlanDetailBinding;
import com.pkhosting.app.databinding.ItemPlanFeatureBinding;
import com.pkhosting.app.models.Order;
import com.pkhosting.app.models.Plan;
import com.pkhosting.app.utils.Constants;
import com.pkhosting.app.utils.CurrencyUtil;

/**
 * Full detail page for one hosting plan, including its feature list
 * and the "Order now" entry point into the (demo) payment flow.
 */
public class PlanDetailActivity extends AppCompatActivity {

    public static final String EXTRA_PLAN_ID = "planId";

    private ActivityPlanDetailBinding binding;
    private Plan plan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPlanDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.toolbar.setNavigationOnClickListener(v -> finish());

        String planId = getIntent().getStringExtra(EXTRA_PLAN_ID);
        if (planId == null || planId.isEmpty()) {
            finish();
            return;
        }
        loadPlan(planId);
    }

    private void loadPlan(String planId) {
        binding.progressPlan.setVisibility(View.VISIBLE);
        binding.contentScroll.setVisibility(View.GONE);
        FirebaseFirestore.getInstance().collection(Constants.COL_PLANS).document(planId).get()
                .addOnSuccessListener(this::showPlan)
                .addOnFailureListener(e -> {
                    binding.progressPlan.setVisibility(View.GONE);
                    binding.textPlanError.setVisibility(View.VISIBLE);
                    binding.textPlanError.setText(getString(R.string.error_load_failed, e.getMessage()));
                });
    }

    private void showPlan(DocumentSnapshot document) {
        binding.progressPlan.setVisibility(View.GONE);
        plan = document.toObject(Plan.class);
        if (plan == null) {
            binding.textPlanError.setVisibility(View.VISIBLE);
            binding.textPlanError.setText(R.string.error_plan_not_found);
            return;
        }
        plan.setId(document.getId());
        binding.contentScroll.setVisibility(View.VISIBLE);

        binding.textPlanName.setText(plan.getName());
        binding.textPlanTagline.setText(plan.getTagline());
        binding.textPlanCategory.setText(plan.getCategory());
        binding.textPlanPrice.setText(CurrencyUtil.format(plan.getPriceFirstYear()));
        binding.textPlanRenewal.setText(getString(R.string.renewal_price_note,
                CurrencyUtil.format(plan.getRenewalPrice())));

        // Hide the badge chip when a plan has no badge.
        if (plan.getBadge() == null || plan.getBadge().isEmpty()) {
            binding.chipPlanBadge.setVisibility(View.GONE);
        } else {
            binding.chipPlanBadge.setVisibility(View.VISIBLE);
            binding.chipPlanBadge.setText(plan.getBadge());
        }

        // Spec row: websites / storage / email accounts.
        String specs;
        if (plan.getWebsites() > 0) {
            specs = getString(R.string.specs_format, plan.getWebsites(), plan.getStorageGb(),
                    plan.getEmailAccounts());
        } else {
            specs = getString(R.string.specs_reseller, plan.getStorageGb());
        }
        binding.textPlanSpecs.setText(specs);

        // Build the feature checklist dynamically from the Firestore array.
        binding.layoutFeatures.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(this);
        for (String feature : plan.getFeatures()) {
            ItemPlanFeatureBinding row = ItemPlanFeatureBinding.inflate(inflater,
                    binding.layoutFeatures, true);
            row.textFeature.setText(feature);
        }

        binding.btnOrderNow.setOnClickListener(v -> startOrder());
    }

    private void startOrder() {
        if (plan == null) return;
        Intent intent = new Intent(this, OrderPaymentActivity.class);
        intent.putExtra(OrderPaymentActivity.EXTRA_TYPE, Order.TYPE_HOSTING);
        intent.putExtra(OrderPaymentActivity.EXTRA_ITEM_NAME, plan.getName());
        intent.putExtra(OrderPaymentActivity.EXTRA_DETAILS,
                getString(R.string.order_details_hosting, plan.getName()));
        intent.putExtra(OrderPaymentActivity.EXTRA_AMOUNT, plan.getPriceFirstYear());
        startActivity(intent);
    }
}
