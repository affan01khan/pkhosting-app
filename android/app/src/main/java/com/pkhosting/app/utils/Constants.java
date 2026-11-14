package com.pkhosting.app.utils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Central place for collection names, brand constants and demo pricing.
 */
public final class Constants {

    // Firestore collection names.
    public static final String COL_USERS = "users";
    public static final String COL_PLANS = "plans";
    public static final String COL_DOMAINS = "domains";
    public static final String COL_ORDERS = "orders";
    public static final String COL_INVOICES = "invoices";
    public static final String COL_TICKETS = "tickets";
    public static final String COL_REPLIES = "replies";
    public static final String COL_FAQS = "faqs";
    public static final String COL_TAKEN_DOMAINS = "takenDomains";

    // Brand / contact details (from pkhosting.com).
    public static final String CURRENCY = "PKR";
    public static final String SUPPORT_PHONE = "(042) 35692952";
    public static final String SUPPORT_PHONE_DIAL = "04235692952";
    public static final String SUPPORT_EMAIL = "support@pkhosting.com";
    public static final String WEBSITE_URL = "https://www.pkhosting.com/";

    // Demo payment settings.
    public static final String[] PAYMENT_METHODS = {
            "JazzCash (Demo)", "Easypaisa (Demo)", "Bank Transfer (Demo)", "Debit / Credit Card (Demo)"};

    // Demo domain pricing: TLD -> price per year in PKR.
    // (.pk is sold at Rs 3,899 for a 2-year term, i.e. Rs 1,950 per year.)
    private static final Map<String, Long> TLD_PRICES = new LinkedHashMap<>();

    static {
        TLD_PRICES.put(".com", 3299L);
        TLD_PRICES.put(".pk", 1950L);
        TLD_PRICES.put(".net", 2999L);
        TLD_PRICES.put(".org", 2999L);
        TLD_PRICES.put(".online", 999L);
    }

    public static Map<String, Long> getTldPrices() {
        return TLD_PRICES;
    }

    /** Price per year for a TLD, with a safe default for unknown TLDs. */
    public static long priceForTld(String tld) {
        Long price = TLD_PRICES.get(tld.toLowerCase());
        return price == null ? 2999L : price;
    }

    private Constants() {
        // No instances.
    }
}
