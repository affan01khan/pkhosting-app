package com.pkhosting.app.models;

import java.io.Serializable;

/**
 * An invoice document from the Firestore "invoices" collection.
 * status is "Paid" or "Unpaid".
 */
public class Invoice implements Serializable {

    public static final String STATUS_PAID = "Paid";
    public static final String STATUS_UNPAID = "Unpaid";

    private String id = "";
    private String number = "";
    private String userId = "";
    private String orderId = "";
    private String description = "";
    private String status = "";
    private String paymentMethod = "";
    private long amount = 0;
    private long dueAt = 0;
    private long createdAt = 0;

    public Invoice() {
        // Required empty constructor for Firestore deserialization.
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getNumber() { return number; }
    public void setNumber(String number) { this.number = number; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public long getAmount() { return amount; }
    public void setAmount(long amount) { this.amount = amount; }
    public long getDueAt() { return dueAt; }
    public void setDueAt(long dueAt) { this.dueAt = dueAt; }
    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
}
