package com.pkhosting.app.models;

import java.io.Serializable;

/**
 * An order document from the Firestore "orders" collection.
 * type is one of: "hosting", "domain", "transfer".
 * status is one of: "Active", "Pending", "Cancelled".
 */
public class Order implements Serializable {

    public static final String TYPE_HOSTING = "hosting";
    public static final String TYPE_DOMAIN = "domain";
    public static final String TYPE_TRANSFER = "transfer";

    public static final String STATUS_ACTIVE = "Active";
    public static final String STATUS_PENDING = "Pending";
    public static final String STATUS_CANCELLED = "Cancelled";

    private String id = "";
    private String orderNumber = "";
    private String userId = "";
    private String type = "";
    private String itemName = "";
    private String details = "";
    private String status = "";
    private String paymentMethod = "";
    private long amount = 0;
    private long createdAt = 0;

    public Order() {
        // Required empty constructor for Firestore deserialization.
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getOrderNumber() { return orderNumber; }
    public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }
    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public long getAmount() { return amount; }
    public void setAmount(long amount) { this.amount = amount; }
    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
}
