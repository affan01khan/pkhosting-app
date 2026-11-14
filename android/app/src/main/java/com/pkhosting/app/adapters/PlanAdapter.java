package com.pkhosting.app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pkhosting.app.R;
import com.pkhosting.app.databinding.ItemPlanBinding;
import com.pkhosting.app.databinding.ItemPlanFeaturedBinding;
import com.pkhosting.app.models.Plan;
import com.pkhosting.app.utils.CurrencyUtil;

import java.util.List;

/**
 * Shows hosting plans. Used in two visual variants:
 *  - full cards (vertical list on the Plans screen)
 *  - compact cards (horizontal carousel on Home)
 */
public class PlanAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    /** Click listener for opening a plan's detail page. */
    public interface OnPlanClickListener {
        void onPlanClick(Plan plan);
    }

    private static final int TYPE_FULL = 0;
    private static final int TYPE_FEATURED = 1;

    private final List<Plan> plans;
    private final boolean featured;
    private final OnPlanClickListener listener;

    public PlanAdapter(List<Plan> plans, boolean featured, OnPlanClickListener listener) {
        this.plans = plans;
        this.featured = featured;
        this.listener = listener;
    }

    /** Replaces the displayed list and redraws. */
    public void replace(List<Plan> newPlans) {
        plans.clear();
        plans.addAll(newPlans);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return featured ? TYPE_FEATURED : TYPE_FULL;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == TYPE_FEATURED) {
            return new FeaturedHolder(ItemPlanFeaturedBinding.inflate(inflater, parent, false));
        }
        return new FullHolder(ItemPlanBinding.inflate(inflater, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Plan plan = plans.get(position);
        if (holder instanceof FullHolder) {
            ((FullHolder) holder).bind(plan);
        } else {
            ((FeaturedHolder) holder).bind(plan);
        }
    }

    @Override
    public int getItemCount() {
        return plans.size();
    }

    /** Spec line under the plan name, e.g. "1 website · 5 GB SSD · 5 emails". */
    private String specLine(Plan plan) {
        if (plan.getWebsites() > 0) {
            return plan.getWebsites() + " website" + (plan.getWebsites() > 1 ? "s" : "")
                    + " · " + plan.getStorageGb() + " GB SSD · "
                    + plan.getEmailAccounts() + " emails";
        }
        return plan.getStorageGb() + " GB SSD · Managed platform";
    }

    class FullHolder extends RecyclerView.ViewHolder {
        private final ItemPlanBinding binding;

        FullHolder(ItemPlanBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Plan plan) {
            binding.textPlanName.setText(plan.getName());
            binding.textPlanTagline.setText(plan.getTagline());
            binding.textPlanSpecs.setText(specLine(plan));
            binding.textPlanPrice.setText(CurrencyUtil.format(plan.getPriceFirstYear()));
            binding.textPlanRenewal.setText(binding.getRoot().getContext()
                    .getString(R.string.renewal_price_short,
                            CurrencyUtil.format(plan.getRenewalPrice())));

            if (plan.getBadge() == null || plan.getBadge().isEmpty()) {
                binding.chipBadge.setVisibility(View.GONE);
            } else {
                binding.chipBadge.setVisibility(View.VISIBLE);
                binding.chipBadge.setText(plan.getBadge());
            }

            binding.getRoot().setOnClickListener(v -> listener.onPlanClick(plan));
        }
    }

    class FeaturedHolder extends RecyclerView.ViewHolder {
        private final ItemPlanFeaturedBinding binding;

        FeaturedHolder(ItemPlanFeaturedBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Plan plan) {
            binding.textPlanName.setText(plan.getName());
            binding.textPlanTagline.setText(plan.getTagline());
            binding.textPlanPrice.setText(CurrencyUtil.format(plan.getPriceFirstYear()));
            binding.textPlanSpecs.setText(specLine(plan));

            if (plan.getBadge() == null || plan.getBadge().isEmpty()) {
                binding.chipBadge.setVisibility(View.GONE);
            } else {
                binding.chipBadge.setVisibility(View.VISIBLE);
                binding.chipBadge.setText(plan.getBadge());
            }

            binding.getRoot().setOnClickListener(v -> listener.onPlanClick(plan));
        }
    }
}
