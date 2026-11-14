package com.pkhosting.app.utils;

import android.util.Patterns;

/**
 * Reusable input validation helpers. Every form in the app funnels
 * through these methods so the rules stay consistent.
 */
public final class Validators {

    private static final String DOMAIN_NAME_REGEX = "^[a-z0-9]([a-z0-9-]{0,61}[a-z0-9])?$";

    /** Email must match the standard Android email pattern. */
    public static boolean isValidEmail(String email) {
        return email != null && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    /** Password must be at least 6 characters (Firebase Auth minimum). */
    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
    }

    /** Phone: 10 to 13 digits, optionally starting with '+'. */
    public static boolean isValidPhone(String phone) {
        return phone != null && phone.matches("^\\+?[0-9]{10,13}$");
    }

    /** Domain name part (before the TLD): lowercase letters, digits, hyphens. */
    public static boolean isValidDomainName(String name) {
        return name != null && name.matches(DOMAIN_NAME_REGEX);
    }

    /** Domain with TLD, e.g. "mysite.com" (used by the transfer form). */
    public static boolean isValidFullDomain(String domain) {
        if (domain == null) return false;
        String d = domain.toLowerCase().trim();
        int dot = d.indexOf('.');
        return dot > 0 && dot < d.length() - 1
                && isValidDomainName(d.substring(0, dot))
                && d.substring(dot).matches("^\\.[a-z.]{2,10}$");
    }

    /** True when the text is not null and not only whitespace. */
    public static boolean isNonEmpty(String text) {
        return text != null && !text.trim().isEmpty();
    }

    /** Demo card number: 16 digits (spaces are ignored). */
    public static boolean isValidCardNumber(String number) {
        return number != null && number.replace(" ", "").matches("^[0-9]{16}$");
    }

    /** Demo card expiry in MM/YY format. */
    public static boolean isValidCardExpiry(String expiry) {
        if (expiry == null || !expiry.matches("^(0[1-9]|1[0-2])/[0-9]{2}$")) return false;
        int month = Integer.parseInt(expiry.substring(0, 2));
        return month >= 1 && month <= 12;
    }

    /** Demo card CVV: 3 or 4 digits. */
    public static boolean isValidCardCvv(String cvv) {
        return cvv != null && cvv.matches("^[0-9]{3,4}$");
    }

    private Validators() {
        // No instances.
    }
}
