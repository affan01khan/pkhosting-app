package com.pkhosting.app.models;

/**
 * Result of a demo domain availability check.
 */
public class DomainResult {

    private final String name;
    private final boolean available;
    private final long pricePerYear;
    private final String message;

    public DomainResult(String name, boolean available, long pricePerYear, String message) {
        this.name = name;
        this.available = available;
        this.pricePerYear = pricePerYear;
        this.message = message;
    }

    public String getName() { return name; }
    public boolean isAvailable() { return available; }
    public long getPricePerYear() { return pricePerYear; }
    public String getMessage() { return message; }
}
