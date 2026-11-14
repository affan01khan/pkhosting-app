package com.pkhosting.app.utils;

import android.os.Handler;
import android.os.Looper;

import java.util.Random;

/**
 * DEMO PAYMENT ENGINE.
 *
 * This class simulates a payment gateway: it validates the chosen method,
 * waits a moment (like a real network round-trip) and then reports success
 * with a fake transaction id. No real money moves and no real credentials
 * or private API keys are ever stored in the app.
 *
 * HOW TO PLUG IN A REAL GATEWAY LATER
 * -----------------------------------
 * JazzCash / Easypaisa / Stripe all require secret keys. Secrets must never
 * live inside the APK. The correct architecture is:
 *
 *   App  ->  Your backend server (holds the secret keys)  ->  Gateway
 *
 * To integrate, create a class such as JazzCashGateway that implements
 * PaymentCallback, call your backend from it, and swap the call inside
 * processDemoPayment() below for the real implementation. The rest of the
 * app already talks only to this class, so nothing else changes.
 */
public final class PaymentManager {

    /** Callback invoked on the main thread when the (demo) payment finishes. */
    public interface PaymentCallback {
        void onResult(boolean success, String txnIdOrError);
    }

    private static final long DEMO_LATENCY_MS = 1500;
    private static final Random RANDOM = new Random();

    /**
     * Simulates a payment for the given amount and method.
     * In Demo Mode every attempt succeeds after a short delay.
     */
    public static void processDemoPayment(String method, long amount, PaymentCallback callback) {
        if (!isMethodKnown(method)) {
            callback.onResult(false, "Unknown payment method");
            return;
        }
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            String txnId = "DEMO-" + String.format("%08d", RANDOM.nextInt(100000000));
            callback.onResult(true, txnId);
        }, DEMO_LATENCY_MS);
    }

    /** True when the method name matches one of the supported demo methods. */
    public static boolean isMethodKnown(String method) {
        for (String m : Constants.PAYMENT_METHODS) {
            if (m.equals(method)) return true;
        }
        return false;
    }

    private PaymentManager() {
        // No instances.
    }
}
