package com.pkhosting.app.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A hosting plan document from the Firestore "plans" collection.
 * Public fields (with a no-argument constructor) let Firestore map
 * documents to this class automatically via toObject().
 */
public class Plan implements Serializable {

    private String id = "";
    private String name = "";
    private String tagline = "";
    private String category = "";
    private String badge = "";
    private long priceFirstYear = 0;
    private long renewalPrice = 0;
    private long websites = 0;
    private long storageGb = 0;
    private long emailAccounts = 0;
    private List<String> features = new ArrayList<>();
    private boolean popular = false;

    public Plan() {
        // Required empty constructor for Firestore deserialization.
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getTagline() { return tagline; }
    public void setTagline(String tagline) { this.tagline = tagline; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getBadge() { return badge; }
    public void setBadge(String badge) { this.badge = badge; }
    public long getPriceFirstYear() { return priceFirstYear; }
    public void setPriceFirstYear(long priceFirstYear) { this.priceFirstYear = priceFirstYear; }
    public long getRenewalPrice() { return renewalPrice; }
    public void setRenewalPrice(long renewalPrice) { this.renewalPrice = renewalPrice; }
    public long getWebsites() { return websites; }
    public void setWebsites(long websites) { this.websites = websites; }
    public long getStorageGb() { return storageGb; }
    public void setStorageGb(long storageGb) { this.storageGb = storageGb; }
    public long getEmailAccounts() { return emailAccounts; }
    public void setEmailAccounts(long emailAccounts) { this.emailAccounts = emailAccounts; }
    public List<String> getFeatures() { return features; }
    public void setFeatures(List<String> features) { this.features = features; }
    public boolean isPopular() { return popular; }
    public void setPopular(boolean popular) { this.popular = popular; }
}
