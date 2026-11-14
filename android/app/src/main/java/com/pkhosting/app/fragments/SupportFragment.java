package com.pkhosting.app.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.pkhosting.app.R;
import com.pkhosting.app.activities.MainActivity;
import com.pkhosting.app.databinding.FragmentSupportBinding;
import com.pkhosting.app.utils.Constants;

/**
 * Support hub: tickets (list + new ticket) and live contact options.
 */
public class SupportFragment extends Fragment {

    public static final String TAG = "SupportFragment";

    private FragmentSupportBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentSupportBinding.inflate(inflater, container, false);

        binding.cardMyTickets.setOnClickListener(v ->
                ((MainActivity) requireActivity()).showFragment(
                        new TicketsFragment(), TicketsFragment.TAG));
        binding.cardNewTicket.setOnClickListener(v ->
                ((MainActivity) requireActivity()).showFragment(
                        new NewTicketFragment(), NewTicketFragment.TAG));

        binding.btnCall.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL,
                    Uri.parse("tel:" + Constants.SUPPORT_PHONE_DIAL));
            startActivity(intent);
        });

        binding.btnEmail.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_SENDTO,
                    Uri.parse("mailto:" + Constants.SUPPORT_EMAIL));
            startActivity(Intent.createChooser(intent, getString(R.string.contact_send_email)));
        });

        // Live chat is not part of the demo; explain the real schedule instead.
        binding.btnLiveChat.setOnClickListener(v -> Snackbar.make(binding.getRoot(),
                R.string.live_chat_note, Snackbar.LENGTH_LONG).show());

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
