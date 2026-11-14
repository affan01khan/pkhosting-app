# PKHosting — Screen List (FYP documentation)

23 screens across 11 activities and 13 fragments. All screens use the strict
brand palette (Primary Blue `#0877D1`, Dark Black `#101820`, White `#FFFFFF`,
Light Blue `#EAF4FC`) with rounded Material 3 cards and no green anywhere.

| # | Screen | Type | Description |
|---|--------|------|-------------|
| 1 | Splash | Activity | Brand logo and tagline for ~1.6 s, then routes to Login (or Main if already signed in). |
| 2 | Login | Activity | Firebase email/password sign-in with inline validation, progress state, links to Register and Forgot Password. |
| 3 | Register | Activity | Creates the Firebase Auth account and writes the user profile (name, email, phone) to the `users` collection. Validates all five fields. |
| 4 | Forgot Password | Activity | Sends a Firebase password-reset email after validating the address. |
| 5 | Main (shell) | Activity | Toolbar + navigation drawer + bottom navigation hosting one fragment at a time. Drawer header shows the signed-in user's name/email. |
| 6 | Home Dashboard | Fragment | Hero banner with promo badge, domain search bar, featured plans carousel (dark cards), and quick-action cards (Register Domain, Transfer, Invoices, Support). |
| 7 | Hosting Plans | Fragment | All plans from Firestore as rounded cards with badge chips, PKR prices, renewal note, spec line and "Order Now". Category chips (All / Shared / Reseller) and a "Compare plans" button. Empty state offers one-tap sample-data loading. |
| 8 | Plan Detail | Activity | Full plan page: badge, name, tagline, category, first-year price, renewal price, spec row and a dynamic feature checklist built from the Firestore array. "Order Now" starts checkout. |
| 9 | Plan Comparison | Fragment | Static side-by-side table comparing Personal / Business / Professional / Reseller across price, websites, storage, bandwidth, email, SSL, backups, DNS, free domain and support. |
| 10 | Domain Search & Registration | Fragment | Tabbed screen. **Register tab**: name + TLD dropdown + years dropdown, demo availability check against `takenDomains`, total price preview, result card with suggestions when taken. **Transfer tab**: domain + EPP/auth-code form that raises a pending transfer order. |
| 11 | Customer Dashboard | Fragment | Greeting with the user's name, three stat cards (active services, domains, unpaid invoices), recent services and domains lists, pull-to-refresh. |
| 12 | Orders | Fragment | Full order history, newest first, with type and status chips and amounts. |
| 13 | Invoices | Fragment | Invoice list with status chips; tap opens the detail screen. |
| 14 | Invoice Detail | Activity | Invoice number, description, amount, issue/due dates and status. Unpaid invoices show a "Pay Now (Demo)" button that marks the invoice paid and activates its order. |
| 15 | Payment | Activity | Clearly-labelled **Demo Mode** checkout: item summary, amount, four payment methods (JazzCash, Easypaisa, Bank Transfer, Card). Card fields appear and are validated only for the card method. Simulated processing then a success dialog with the demo transaction id; the Order + Invoice (+ Domain) are written atomically. |
| 16 | Support Hub | Fragment | Cards to My Tickets and New Ticket, plus call / email / live-chat buttons with support hours. |
| 17 | Tickets | Fragment | Support ticket list with status chips and a "New Ticket" floating button; swipe to refresh. |
| 18 | New Ticket | Fragment | Category dropdown, subject and message fields with validation; writes a ticket with status "Open". |
| 19 | Ticket Detail | Activity | Full ticket view plus its replies conversation (read from the `replies` sub-collection) and a reply composer. |
| 20 | Contact | Activity | Real PK Hosting phone, email and website with tap-to-call, email chooser and browser links. |
| 21 | FAQs / Knowledgebase | Fragment | Live text search, category chips (All / Hosting / Billing / Support), expandable Q&A cards. Empty state offers sample-data loading. |
| 22 | Profile | Fragment | Name/email header card, phone and member-since rows, Edit Profile and Log Out actions. |
| 23 | Edit Profile | Activity | Editable name and phone (email read-only), validated and saved to Firestore. |
| 24 | Settings | Fragment | Notification preference (stored locally), one-tap sample-data seeder, support contact info, app version and logout. |

## Shared UX components

- **`include_list_states.xml`** — one reusable component that switches every
  list screen between loading spinner, content, empty message (with optional
  action button) and error (with retry). Used by Plans, Orders, Invoices,
  Tickets, FAQs and Dashboard.
- **Validation** — every form validates input before any network call and
  shows inline errors on the field.
- **Loading states** — every button that triggers a network call shows a
  progress indicator and disables itself.
- **Empty states** — every list explains what will appear there and, where
  relevant, offers the sample-data seeder.
