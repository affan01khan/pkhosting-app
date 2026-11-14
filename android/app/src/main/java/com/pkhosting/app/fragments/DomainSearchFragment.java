package com.pkhosting.app.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.FirebaseFirestore;
import com.pkhosting.app.R;
import com.pkhosting.app.activities.OrderPaymentActivity;
import com.pkhosting.app.databinding.FragmentDomainSearchBinding;
import com.pkhosting.app.models.DomainResult;
import com.pkhosting.app.models.Order;
import com.pkhosting.app.utils.Constants;
import com.pkhosting.app.utils.CurrencyUtil;
import com.pkhosting.app.utils.OrderUtils;
import com.pkhosting.app.utils.Validators;


/**
 * Domains tab with two panels switched by a TabLayout:
 *  - Search: demo availability check against the public "takenDomains"
 *    collection, with pricing and a Register button.
 *  - Transfer: collects the domain and its EPP/auth code and raises a
 *    pending transfer order.
 */
public class DomainSearchFragment extends Fragment {

    public static final String TAG = "DomainSearchFragment";
    private static final String ARG_QUERY = "query";
    private static final String ARG_TRANSFER = "transfer";

    private FragmentDomainSearchBinding binding;
    private FirebaseFirestore db;
    private String selectedTld = ".com";
    private long selectedYears = 1;

    /** Builds the fragment with an optional pre-filled search query. */
    public static DomainSearchFragment newInstance(String query, boolean transferTab) {
        DomainSearchFragment fragment = new DomainSearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_QUERY, query);
        args.putBoolean(ARG_TRANSFER, transferTab);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentDomainSearchBinding.inflate(inflater, container, false);
        db = FirebaseFirestore.getInstance();

        setupTldDropdown();
        setupYearsDropdown();

        binding.btnSearchDomain.setOnClickListener(v -> checkAvailability());
        binding.btnRegisterDomain.setOnClickListener(v -> registerDomain());
        binding.btnTransferDomain.setOnClickListener(v -> requestTransfer());

        // Tab switching between the search and transfer panels.
        binding.tabDomains.addOnTabSelectedListener(new com.google.android.material.tabs.TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(com.google.android.material.tabs.TabLayout.Tab tab) {
                boolean transfer = tab.getPosition() == 1;
                binding.panelSearch.setVisibility(transfer ? View.GONE : View.VISIBLE);
                binding.panelTransfer.setVisibility(transfer ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onTabUnselected(com.google.android.material.tabs.TabLayout.Tab tab) { }

            @Override
            public void onTabReselected(com.google.android.material.tabs.TabLayout.Tab tab) { }
        });

        // Apply arguments from Home (pre-filled query / transfer tab).
        Bundle args = getArguments();
        if (args != null) {
            String query = args.getString(ARG_QUERY, "");
            boolean transferTab = args.getBoolean(ARG_TRANSFER, false);
            if (transferTab) {
                binding.tabDomains.selectTab(binding.tabDomains.getTabAt(1));
            }
            if (Validators.isNonEmpty(query)) {
                binding.editDomainName.setText(query);
                checkAvailability();
            }
        }

        return binding.getRoot();
    }

    private void setupTldDropdown() {
        String[] tlds = Constants.getTldPrices().keySet().toArray(new String[0]);
        binding.dropdownTld.setSimpleItems(tlds);
        binding.dropdownTld.setText(tlds[0], false);
        binding.dropdownTld.setOnItemClickListener((parent, view, position, id) -> {
            selectedTld = tlds[position];
            updatePricePreview();
        });
    }

    private void setupYearsDropdown() {
        String[] yearsOptions = {"1 year", "2 years", "3 years"};
        binding.dropdownYears.setSimpleItems(yearsOptions);
        binding.dropdownYears.setText(yearsOptions[0], false);
        binding.dropdownYears.setOnItemClickListener((parent, view, position, id) -> {
            selectedYears = position + 1;
            updatePricePreview();
        });
    }

    private void updatePricePreview() {
        String name = binding.editDomainName.getText().toString().trim().toLowerCase();
        if (Validators.isValidDomainName(name)) {
            long total = Constants.priceForTld(selectedTld) * selectedYears;
            binding.textRegisterPrice.setText(getString(R.string.domain_price_preview,
                    CurrencyUtil.format(total)));
        }
    }

    /** Checks the public "takenDomains" collection for the requested name. */
    private void checkAvailability() {
        String name = binding.editDomainName.getText().toString().trim().toLowerCase();
        if (!Validators.isValidDomainName(name)) {
            binding.inputDomainName.setError(getString(R.string.error_domain_name));
            return;
        }
        binding.inputDomainName.setError(null);

        String fullDomain = name + selectedTld;
        long pricePerYear = Constants.priceForTld(selectedTld);

        binding.progressSearch.setVisibility(View.VISIBLE);
        binding.layoutResult.setVisibility(View.GONE);
        db.collection(Constants.COL_TAKEN_DOMAINS)
                .whereEqualTo("domainName", fullDomain).limit(1).get()
                .addOnSuccessListener(snapshot -> {
                    binding.progressSearch.setVisibility(View.GONE);
                    DomainResult result;
                    if (snapshot.isEmpty()) {
                        result = new DomainResult(fullDomain, true, pricePerYear, null);
                    } else {
                        result = new DomainResult(fullDomain, false, 0,
                                getString(R.string.domain_taken_message));
                    }
                    showResult(result, name);
                })
                .addOnFailureListener(e -> {
                    binding.progressSearch.setVisibility(View.GONE);
                    Toast.makeText(requireContext(),
                            getString(R.string.error_search_failed, e.getMessage()),
                            Toast.LENGTH_LONG).show();
                });
    }

    private void showResult(DomainResult result, String bareName) {
        binding.layoutResult.setVisibility(View.VISIBLE);
        binding.textResultDomain.setText(result.getName());

        if (result.isAvailable()) {
            binding.iconResultStatus.setImageResource(R.drawable.ic_check_circle);
            binding.textResultStatus.setText(R.string.domain_available);
            binding.layoutAvailableActions.setVisibility(View.VISIBLE);
            long total = result.getPricePerYear() * selectedYears;
            binding.textRegisterPrice.setText(getString(R.string.domain_price_preview,
                    CurrencyUtil.format(total)));
        } else {
            binding.iconResultStatus.setImageResource(R.drawable.ic_info);
            binding.textResultStatus.setText(R.string.domain_taken);
            binding.layoutAvailableActions.setVisibility(View.GONE);
            binding.textSuggestion.setText(getString(R.string.domain_suggestions,
                    bareName + ".pk", bareName + ".net", "get" + bareName + ".com"));
        }
    }

    private void registerDomain() {
        String name = binding.editDomainName.getText().toString().trim().toLowerCase();
        String fullDomain = name + selectedTld;
        long amount = Constants.priceForTld(selectedTld) * selectedYears;

        Intent intent = new Intent(requireContext(), OrderPaymentActivity.class);
        intent.putExtra(OrderPaymentActivity.EXTRA_TYPE, Order.TYPE_DOMAIN);
        intent.putExtra(OrderPaymentActivity.EXTRA_ITEM_NAME, fullDomain);
        intent.putExtra(OrderPaymentActivity.EXTRA_DETAILS,
                getString(R.string.order_details_domain, selectedYears));
        intent.putExtra(OrderPaymentActivity.EXTRA_AMOUNT, amount);
        intent.putExtra(OrderPaymentActivity.EXTRA_DOMAIN_NAME, fullDomain);
        intent.putExtra(OrderPaymentActivity.EXTRA_YEARS, selectedYears);
        startActivity(intent);
    }

    /** Creates a pending transfer order + unpaid invoice (demo). */
    private void requestTransfer() {
        String domain = binding.editTransferDomain.getText().toString().trim().toLowerCase();
        String authCode = binding.editAuthCode.getText().toString().trim();

        boolean valid = true;
        if (!Validators.isValidFullDomain(domain)) {
            binding.inputTransferDomain.setError(getString(R.string.error_full_domain));
            valid = false;
        } else binding.inputTransferDomain.setError(null);
        if (!Validators.isNonEmpty(authCode) || authCode.length() < 6) {
            binding.inputAuthCode.setError(getString(R.string.error_auth_code));
            valid = false;
        } else binding.inputAuthCode.setError(null);
        if (!valid) return;

        binding.progressTransfer.setVisibility(View.VISIBLE);
        binding.btnTransferDomain.setEnabled(false);

        long amount = Constants.priceForTld(domain.substring(domain.indexOf('.')));
        String uid = com.google.firebase.auth.FirebaseAuth.getInstance().getCurrentUser().getUid();

        OrderUtils.placeOrder(db, uid, Order.TYPE_TRANSFER, domain,
                getString(R.string.order_details_transfer), amount,
                Order.STATUS_PENDING, com.pkhosting.app.models.Invoice.STATUS_UNPAID,
                "Bank Transfer (Demo)", null,
                (orderId, invoiceId, error) -> {
                    if (!isAdded()) return;
                    binding.progressTransfer.setVisibility(View.GONE);
                    binding.btnTransferDomain.setEnabled(true);
                    if (error != null) {
                        Toast.makeText(requireContext(),
                                getString(R.string.error_order_failed, error),
                                Toast.LENGTH_LONG).show();
                        return;
                    }
                    Toast.makeText(requireContext(), R.string.transfer_requested,
                            Toast.LENGTH_LONG).show();
                    binding.editTransferDomain.setText("");
                    binding.editAuthCode.setText("");
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refresh the price preview when returning from checkout.
        updatePricePreview();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
