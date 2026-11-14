package com.pkhosting.app.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.pkhosting.app.R;
import com.pkhosting.app.databinding.ActivityContactBinding;
import com.pkhosting.app.utils.Constants;

/**
 * Contact page: PK Hosting phone, email, website and support hours.
 * The buttons open the dialer / email client / browser.
 */
public class ContactActivity extends AppCompatActivity {

    private ActivityContactBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityContactBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.toolbar.setNavigationOnClickListener(v -> finish());

        binding.textPhone.setText(Constants.SUPPORT_PHONE);
        binding.textEmail.setText(Constants.SUPPORT_EMAIL);

        binding.cardCall.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL,
                    Uri.parse("tel:" + Constants.SUPPORT_PHONE_DIAL));
            startActivity(intent);
        });

        binding.cardEmail.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_SENDTO,
                    Uri.parse("mailto:" + Constants.SUPPORT_EMAIL));
            startActivity(Intent.createChooser(intent, getString(R.string.contact_send_email)));
        });

        binding.cardWebsite.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.WEBSITE_URL));
            startActivity(intent);
        });
    }
}
