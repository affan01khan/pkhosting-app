package com.pkhosting.app.utils;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;
import com.pkhosting.app.models.DomainItem;
import com.pkhosting.app.models.Invoice;
import com.pkhosting.app.models.Order;

import java.util.Random;

/**
 * Creates an order and its matching invoice in one atomic Firestore batch.
 * Used by the hosting checkout, the domain registration and the domain
 * transfer flows so all of them stay consistent.
 */
public final class OrderUtils {

    private static final Random RANDOM = new Random();

    /** Called when the batch has been written (or failed). */
    public interface OrderCallback {
        void onDone(String orderId, String invoiceId, @Nullable String error);
    }

    /**
     * Writes Order + Invoice (+ optional DomainItem) in a single batch.
     *
     * @param domain nullable - pass a prepared DomainItem for domain registrations.
     */
    public static void placeOrder(FirebaseFirestore db, String uid, String type, String itemName,
                                  String details, long amount, String orderStatus,
                                  String invoiceStatus, String paymentMethod,
                                  @Nullable DomainItem domain, OrderCallback callback) {
        long now = System.currentTimeMillis();

        Order order = new Order();
        order.setUserId(uid);
        order.setType(type);
        order.setItemName(itemName);
        order.setDetails(details);
        order.setStatus(orderStatus);
        order.setPaymentMethod(paymentMethod);
        order.setAmount(amount);
        order.setCreatedAt(now);

        Invoice invoice = new Invoice();
        invoice.setUserId(uid);
        invoice.setDescription(itemName);
        invoice.setStatus(invoiceStatus);
        invoice.setPaymentMethod(paymentMethod);
        invoice.setAmount(amount);
        invoice.setCreatedAt(now);
        invoice.setDueAt(DateUtils.plusDays(now, 7));

        WriteBatch batch = db.batch();
        DocumentReference orderRef = db.collection(Constants.COL_ORDERS).document();
        order.setOrderNumber("PK-" + (100000 + RANDOM.nextInt(900000)));
        invoice.setOrderId(orderRef.getId());
        invoice.setNumber("INV-" + (100000 + RANDOM.nextInt(900000)));
        batch.set(orderRef, order);

        DocumentReference invoiceRef = db.collection(Constants.COL_INVOICES).document();
        batch.set(invoiceRef, invoice);

        if (domain != null) {
            domain.setUserId(uid);
            batch.set(db.collection(Constants.COL_DOMAINS).document(), domain);
        }

        batch.commit()
                .addOnSuccessListener(unused -> callback.onDone(orderRef.getId(), invoiceRef.getId(), null))
                .addOnFailureListener(e -> callback.onDone(null, null, e.getMessage()));
    }

    private OrderUtils() {
        // No instances.
    }
}
