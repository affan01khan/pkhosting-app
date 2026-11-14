package com.pkhosting.app.utils;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.pkhosting.app.models.Faq;
import com.pkhosting.app.models.Plan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Seeds the Firestore database with sample plans, FAQs and taken domain
 * names so the app has content on first run. Only empty collections are
 * filled, so existing data is never overwritten.
 *
 * The same data can also be entered by hand in the Firebase console -
 * this utility simply automates it for the FYP demo.
 */
public final class FirestoreSeeder {

    /** Called when seeding finishes. error is null on success. */
    public interface SeedCallback {
        void onDone(int documentsWritten, @Nullable String error);
    }

    /** Seeds plans, FAQs and taken domains (only where collections are empty). */
    public static void seedAll(FirebaseFirestore db, SeedCallback callback) {
        final int[] written = {0};
        final String[] error = {null};

        seedPlans(db, (c1, e1) -> {
            written[0] += c1;
            if (e1 != null) error[0] = e1;
            seedFaqs(db, (c2, e2) -> {
                written[0] += c2;
                if (e2 != null) error[0] = e2;
                seedTakenDomains(db, (c3, e3) -> {
                    written[0] += c3;
                    if (e3 != null) error[0] = e3;
                    callback.onDone(written[0], error[0]);
                });
            });
        });
    }

    /** Writes the four PK Hosting plans if the "plans" collection is empty. */
    public static void seedPlans(FirebaseFirestore db, SeedCallback callback) {
        db.collection(Constants.COL_PLANS).get().addOnSuccessListener(snapshot -> {
            if (!snapshot.isEmpty()) {
                callback.onDone(0, null);
                return;
            }
            WriteBatch batch = db.batch();

            Plan personal = new Plan();
            personal.setName("Personal Hosting");
            personal.setTagline("For students, hobbyists & individuals");
            personal.setCategory("Shared Hosting");
            personal.setBadge("Economical");
            personal.setPriceFirstYear(4490);
            personal.setRenewalPrice(6990);
            personal.setWebsites(1);
            personal.setStorageGb(5);
            personal.setEmailAccounts(5);
            personal.setPopular(false);
            personal.setFeatures(planFeatures(1, 5, 5, "1-day retention (latest daily copy)",
                    "WordPress Setup Help", "Email + Database Support"));
            batch.set(db.collection(Constants.COL_PLANS).document("personal"), personal);

            Plan business = new Plan();
            business.setName("Business Hosting");
            business.setTagline("For growing businesses & teams");
            business.setCategory("Shared Hosting");
            business.setBadge("Most Popular");
            business.setPriceFirstYear(11990);
            business.setRenewalPrice(19990);
            business.setWebsites(5);
            business.setStorageGb(25);
            business.setEmailAccounts(25);
            business.setPopular(true);
            business.setFeatures(planFeatures(5, 25, 25, "3-day retention",
                    "Priority Setup", "Managed DNS Help", "Business Email Support"));
            batch.set(db.collection(Constants.COL_PLANS).document("business"), business);

            Plan professional = new Plan();
            professional.setName("Professional Hosting");
            professional.setTagline("For software houses & corporates");
            professional.setCategory("Shared Hosting");
            professional.setBadge("Best Value");
            professional.setPriceFirstYear(23990);
            professional.setRenewalPrice(39990);
            professional.setWebsites(10);
            professional.setStorageGb(50);
            professional.setEmailAccounts(50);
            professional.setPopular(true);
            professional.setFeatures(planFeatures(10, 50, 50, "5-day retention",
                    "No fixed LVE allocation - fair-use CPU, RAM and I/O",
                    "Priority Support", "Developer-Friendly Setup"));
            batch.set(db.collection(Constants.COL_PLANS).document("professional"), professional);

            Plan reseller = new Plan();
            reseller.setName("Reseller R2");
            reseller.setTagline("Best storage-to-price fit for a growing client list");
            reseller.setCategory("Reseller Hosting");
            reseller.setBadge("Most Popular");
            reseller.setPriceFirstYear(23990);
            reseller.setRenewalPrice(29990);
            reseller.setWebsites(0);
            reseller.setStorageGb(60);
            reseller.setEmailAccounts(0);
            reseller.setPopular(false);
            List<String> resellerFeatures = new ArrayList<>();
            resellerFeatures.add("Free migration");
            resellerFeatures.add("60 GB SSD Storage");
            resellerFeatures.add("Unmetered bandwidth - fair-use policy applies");
            resellerFeatures.add("Managed Hosting Platform");
            resellerFeatures.add("Managed Account Setup");
            resellerFeatures.add("Client Dashboard");
            resellerFeatures.add("Private Nameservers");
            resellerFeatures.add("Free SSL for All Accounts");
            resellerFeatures.add("Priority Support");
            reseller.setFeatures(resellerFeatures);
            batch.set(db.collection(Constants.COL_PLANS).document("reseller-r2"), reseller);

            commitBatch(batch, 4, callback);
        }).addOnFailureListener(e -> callback.onDone(0, e.getMessage()));
    }

    /** Shared feature list builder for the shared hosting plans. */
    private static List<String> planFeatures(int websites, int storage, int emails,
                                             String backupNote, String... extras) {
        List<String> features = new ArrayList<>();
        features.add("Free migration");
        features.add(websites + " Website" + (websites > 1 ? "s" : ""));
        features.add(storage + " GB SSD Storage");
        features.add("Unmetered bandwidth - fair-use policy applies");
        features.add(emails + " Email Accounts");
        features.add("Free .com with a 3-year term");
        features.add("Free SSL Certificate");
        features.add("Daily backups - " + backupNote);
        for (String extra : extras) {
            features.add(extra);
        }
        features.add("PK Hosting Dashboard");
        return features;
    }

    /** Writes sample knowledgebase entries if the "faqs" collection is empty. */
    public static void seedFaqs(FirebaseFirestore db, SeedCallback callback) {
        db.collection(Constants.COL_FAQS).get().addOnSuccessListener(snapshot -> {
            if (!snapshot.isEmpty()) {
                callback.onDone(0, null);
                return;
            }
            WriteBatch batch = db.batch();

            faq(batch, db, "What is shared hosting?",
                    "Our shared hosting is Linux hosting for websites, PHP applications and "
                            + "supported databases. It does not include Windows Server, IIS or Microsoft "
                            + "SQL Server - for those, choose a Windows VPS.", "Hosting");
            faq(batch, db, "What are the storage limits of each plan?",
                    "Personal includes 1 website and 5 GB SSD, Business 5 websites and 25 GB SSD, "
                            + "and Professional 10 websites and 50 GB SSD. Storage is shared by your "
                            + "account's website files, databases and email.", "Hosting");
            faq(batch, db, "Is there a bandwidth limit?",
                    "There is no fixed monthly transfer quota. Unmetered bandwidth is subject to a "
                            + "fair-use policy: sustained abusive traffic or excessive CPU/I/O use may "
                            + "require an upgrade or restriction.", "Hosting");
            faq(batch, db, "Can I run WordPress?",
                    "Yes. All shared plans can run WordPress with one-click setup help and migration "
                            + "support. Dedicated WordPress plans (WP Starter, WP Business, WP Pro) are "
                            + "also available.", "Hosting");
            faq(batch, db, "Is SSL included?",
                    "Yes - free SSL support is included with every hosting plan, along with secure "
                            + "defaults and practical guidance for keeping your site protected.", "Hosting");
            faq(batch, db, "How do daily backups work?",
                    "Daily backups cover files, databases and email. Personal keeps the latest daily "
                            + "copy (1-day retention), Business 3 days and Professional 5 days. Restores "
                            + "from copies still listed in the dashboard are included.", "Hosting");
            faq(batch, db, "Which payment methods can I use?",
                    "Checkout supports JazzCash, Easypaisa, bank transfer and debit/credit cards. "
                            + "In this companion app all payments run in Demo Mode - no real money is "
                            + "charged.", "Billing");
            faq(batch, db, "How do I contact support?",
                    "Call (042) 35692952, email support@pkhosting.com or open a ticket from the app. "
                            + "Live chat and the direct line run Mon-Fri, 9am-6pm PKT; tickets are "
                            + "answered every day.", "Support");

            commitBatch(batch, 8, callback);
        }).addOnFailureListener(e -> callback.onDone(0, e.getMessage()));
    }

    private static void faq(WriteBatch batch, FirebaseFirestore db, String question,
                            String answer, String category) {
        Faq f = new Faq();
        f.setQuestion(question);
        f.setAnswer(answer);
        f.setCategory(category);
        batch.set(db.collection(Constants.COL_FAQS).document(), f);
    }

    /** Domain names that are already registered (for the availability demo). */
    public static void seedTakenDomains(FirebaseFirestore db, SeedCallback callback) {
        db.collection(Constants.COL_TAKEN_DOMAINS).get().addOnSuccessListener((QuerySnapshot s) -> {
            if (!s.isEmpty()) {
                callback.onDone(0, null);
                return;
            }
            WriteBatch batch = db.batch();
            String[] taken = {"pkhosting.com", "google.com", "facebook.com", "daraz.pk",
                    "wikipedia.org", "example.com", "youtube.com", "github.io"};
            for (String name : taken) {
                Map<String, Object> doc = new HashMap<>();
                doc.put("domainName", name);
                batch.set(db.collection(Constants.COL_TAKEN_DOMAINS).document(), doc);
            }
            commitBatch(batch, taken.length, callback);
        }).addOnFailureListener(e -> callback.onDone(0, e.getMessage()));
    }

    private static void commitBatch(WriteBatch batch, int count, SeedCallback callback) {
        batch.commit()
                .addOnSuccessListener(unused -> callback.onDone(count, null))
                .addOnFailureListener(e -> callback.onDone(0, e.getMessage()));
    }

    private FirestoreSeeder() {
        // No instances.
    }
}
