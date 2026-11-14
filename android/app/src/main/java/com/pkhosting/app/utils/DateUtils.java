package com.pkhosting.app.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Date helpers for timestamps stored as milliseconds since epoch.
 */
public final class DateUtils {

    private static final SimpleDateFormat DAY_MONTH_YEAR =
            new SimpleDateFormat("dd MMM yyyy", Locale.US);

    /** 1739232000000 -> "11 Feb 2025". */
    public static String formatDate(long millis) {
        return DAY_MONTH_YEAR.format(new Date(millis));
    }

    /** Adds the given number of days to a timestamp. */
    public static long plusDays(long millis, int days) {
        return millis + TimeUnit.DAYS.toMillis(days);
    }

    /** Adds the given number of years to a timestamp. */
    public static long plusYears(long millis, long years) {
        return millis + TimeUnit.DAYS.toMillis(365L * years);
    }

    private DateUtils() {
        // No instances.
    }
}
