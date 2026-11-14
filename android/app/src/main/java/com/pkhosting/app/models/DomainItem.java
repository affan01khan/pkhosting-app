package com.pkhosting.app.models;

/**
 * A domain registered by the customer, from the Firestore "domains" collection.
 */
public class DomainItem {

    private String id = "";
    private String userId = "";
    private String domainName = "";
    private String status = "";
    private long years = 1;
    private long amount = 0;
    private long registeredAt = 0;
    private long expiryAt = 0;
    private boolean autoRenew = false;

    public DomainItem() {
        // Required empty constructor for Firestore deserialization.
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getDomainName() { return domainName; }
    public void setDomainName(String domainName) { this.domainName = domainName; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public long getYears() { return years; }
    public void setYears(long years) { this.years = years; }
    public long getAmount() { return amount; }
    public void setAmount(long amount) { this.amount = amount; }
    public long getRegisteredAt() { return registeredAt; }
    public void setRegisteredAt(long registeredAt) { this.registeredAt = registeredAt; }
    public long getExpiryAt() { return expiryAt; }
    public void setExpiryAt(long expiryAt) { this.expiryAt = expiryAt; }
    public boolean isAutoRenew() { return autoRenew; }
    public void setAutoRenew(boolean autoRenew) { this.autoRenew = autoRenew; }
}
