# Firebase Setup Guide (step by step)

Follow these steps once, before running the app. Takes about 10 minutes.

## 1. Create the Firebase project

1. Go to <https://console.firebase.google.com/> and sign in with a Google account.
2. Click **Create a project** (or "Add project").
3. Name it e.g. `pkhosting-fyp` and continue.
4. Google Analytics is optional — you can disable it.
5. Wait for the project to be created, then click **Continue**.

## 2. Register the Android app

1. On the project overview page, click the **Android icon** to add an app.
2. **Android package name** must be exactly:

   ```
   com.pkhosting.app
   ```

3. App nickname: `PKHosting` (anything). SHA-1 is **not** required for
   email/password auth — leave it empty.
4. Click **Register app**.

## 3. Download google-services.json

1. After registering, click **Download google-services.json**.
2. Replace the placeholder file in the project:

   ```
   PKHosting/app/google-services.json   ← overwrite this file
   ```

3. Skip the "Add Firebase SDK" steps in the wizard — the Gradle files in this
   project already include the google-services plugin and dependencies.
4. Click **Continue to console**.

## 4. Enable Email/Password authentication

1. In the Firebase console, open **Build ▸ Authentication ▸ Get started**.
2. Choose **Email/Password**, toggle **Enable**, and click **Save**.

## 5. Create the Firestore database

1. Open **Build ▸ Firestore Database ▸ Create database**.
2. Choose **Start in test mode** is fine for the demo, but we will replace the
   rules in the next step anyway.
3. Pick a location close to you (e.g. `asia-south1` (Mumbai) is closest to
   Pakistan) and click **Enable**.

## 6. Publish the security rules

1. In Firestore, open the **Rules** tab.
2. Replace the contents with the rules from the project file
   [`firestore.rules`](../firestore.rules) (copy the whole file).
3. Click **Publish**.

What the rules enforce:

- `users/{uid}` — a user can read/write **only their own** profile document.
- `orders`, `invoices`, `domains`, `tickets` — a document can only be read or
  updated by the user whose `userId` is stored on it, and can only be created
  with the signed-in user's own `userId`.
- `tickets/{id}/replies/{replyId}` — only the ticket owner can read or add replies.
- `plans`, `faqs`, `takenDomains` — public catalog data: anyone can read;
  signed-in users may write so the in-app **Load sample data** seeder works
  (tighten this for production).

## 7. Load the sample data

1. Build and run the app in Android Studio (Run ▸ app).
2. Register a new account on the Register screen.
3. Open **Settings ▸ Load sample data**. The app writes:
   - 4 hosting plans (Personal, Business, Professional, Reseller R2) into `plans`
   - 8 FAQs into `faqs`
   - 8 "already taken" domain names into `takenDomains`
4. Verify in the Firebase console ▸ Firestore ▸ Data that the collections
   appeared. The seeder only fills **empty** collections, so it is safe to
   tap again.

## 8. Test everything works

- Log out and log back in with the account you created.
- The Home screen should show featured plans (Business + Professional).
- Plans screen shows all four plans; Domain search should report
  `pkhosting.com` as taken and a random name as available.
- Place a demo order (any plan ▸ Order now ▸ Pay Now) — it appears in
  Orders and creates a paid invoice.
- Open a support ticket and reply to it.

## Troubleshooting

| Problem | Fix |
|---|---|
| `Google Play Services failed to initialize` / app crashes on launch | `google-services.json` is missing, still the placeholder, or has the wrong package name — repeat step 3. |
| `PERMISSION_DENIED` from Firestore | Rules from step 6 were not published, or you are testing while logged out. |
| Plans/FAQ screens show "no data" | Run **Settings ▸ Load sample data** (step 7). |
| Gradle sync fails on `google-services` plugin | Make sure you are online so the plugin can download from `google()`, and use Android Studio Hedgehog or newer (AGP 8.2). |
