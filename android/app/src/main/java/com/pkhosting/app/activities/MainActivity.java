package com.pkhosting.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.pkhosting.app.R;
import com.pkhosting.app.databinding.ActivityMainBinding;
import com.pkhosting.app.fragments.DashboardFragment;
import com.pkhosting.app.fragments.DomainSearchFragment;
import com.pkhosting.app.fragments.FaqFragment;
import com.pkhosting.app.fragments.HomeFragment;
import com.pkhosting.app.fragments.InvoicesFragment;
import com.pkhosting.app.fragments.OrdersFragment;
import com.pkhosting.app.fragments.PlansFragment;
import com.pkhosting.app.fragments.ProfileFragment;
import com.pkhosting.app.fragments.SettingsFragment;
import com.pkhosting.app.fragments.SupportFragment;
import com.pkhosting.app.models.AppUser;
import com.pkhosting.app.utils.AuthUtils;
import com.pkhosting.app.utils.Constants;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

/**
 * Main shell of the app: a toolbar, a navigation drawer, a bottom
 * navigation bar and a container that hosts one fragment at a time.
 */
public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private boolean suppressNavSelection = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.toolbar.setNavigationOnClickListener(v ->
                binding.drawerLayout.openDrawer(GravityCompat.START));

        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            if (suppressNavSelection) return true;
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                switchFragment(new HomeFragment(), HomeFragment.TAG);
            } else if (id == R.id.nav_plans) {
                switchFragment(new PlansFragment(), PlansFragment.TAG);
            } else if (id == R.id.nav_domains) {
                switchFragment(DomainSearchFragment.newInstance("", false), DomainSearchFragment.TAG);
            } else if (id == R.id.nav_dashboard) {
                switchFragment(new DashboardFragment(), DashboardFragment.TAG);
            } else if (id == R.id.nav_support) {
                switchFragment(new SupportFragment(), SupportFragment.TAG);
            } else {
                return false;
            }
            return true;
        });
        binding.bottomNavigation.setOnItemReselectedListener(item -> { /* ignore re-taps */ });

        binding.navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.drawer_profile) {
                switchFragment(new ProfileFragment(), ProfileFragment.TAG);
            } else if (id == R.id.drawer_orders) {
                switchFragment(new OrdersFragment(), OrdersFragment.TAG);
            } else if (id == R.id.drawer_invoices) {
                switchFragment(new InvoicesFragment(), InvoicesFragment.TAG);
            } else if (id == R.id.drawer_faqs) {
                switchFragment(new FaqFragment(), FaqFragment.TAG);
            } else if (id == R.id.drawer_contact) {
                startActivity(new Intent(this, ContactActivity.class));
            } else if (id == R.id.drawer_settings) {
                switchFragment(new SettingsFragment(), SettingsFragment.TAG);
            } else if (id == R.id.drawer_logout) {
                confirmLogout();
            }
            binding.drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

        loadHeaderUser();

        if (savedInstanceState == null) {
            switchFragment(new HomeFragment(), HomeFragment.TAG);
        }
    }

    /** Replaces the content fragment. Also used by child fragments for navigation. */
    public void showFragment(Fragment fragment, String tag) {
        switchFragment(fragment, tag);
    }

    /** Opens the Domains tab, optionally pre-filling the search or transfer tab. */
    public void openDomains(String query, boolean transferTab) {
        suppressNavSelection = true;
        binding.bottomNavigation.setSelectedItemId(R.id.nav_domains);
        suppressNavSelection = false;
        switchFragment(DomainSearchFragment.newInstance(query, transferTab), DomainSearchFragment.TAG);
    }

    /** Selects a bottom navigation tab programmatically. */
    public void selectTab(int itemId) {
        suppressNavSelection = true;
        binding.bottomNavigation.setSelectedItemId(itemId);
        suppressNavSelection = false;
    }

    /** Opens the plan comparison table on top of the current fragment. */
    public void openPlanComparison() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, new com.pkhosting.app.fragments.PlanComparisonFragment())
                .addToBackStack(null)
                .commit();
    }

    private void switchFragment(Fragment fragment, String tag) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, fragment, tag)
                .commit();
    }

    /** Loads the signed-in user's profile into the drawer header. */
    private void loadHeaderUser() {
        FirebaseUser firebaseUser = com.google.firebase.auth.FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null) return;

        View header = binding.navigationView.getHeaderView(0);
        TextView name = header.findViewById(R.id.textHeaderName);
        TextView email = header.findViewById(R.id.textHeaderEmail);

        FirebaseFirestore.getInstance().collection(Constants.COL_USERS)
                .document(firebaseUser.getUid()).get()
                .addOnSuccessListener(doc -> {
                    AppUser user = doc.toObject(AppUser.class);
                    if (user != null && !user.getName().isEmpty()) {
                        name.setText(user.getName());
                        email.setText(user.getEmail());
                    } else {
                        name.setText(R.string.guest_user);
                        email.setText(firebaseUser.getEmail());
                    }
                });
    }

    private void confirmLogout() {
        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.logout_title)
                .setMessage(R.string.logout_message)
                .setPositiveButton(R.string.action_logout, (d, w) -> AuthUtils.logout(this))
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }
}
