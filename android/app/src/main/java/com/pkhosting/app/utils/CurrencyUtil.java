package com.pkhosting.app.utils;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Formats amounts in Pakistani Rupees, e.g. 11990 -> "Rs 11,990".
 */
public final class CurrencyUtil {

    public static String format(long amount) {
        NumberFormat nf = NumberFormat.getInstance(Locale.US);
        return "Rs " + nf.format(amount);
    }

    private CurrencyUtil() {
        // No instances.
    }
}
