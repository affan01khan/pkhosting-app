# PKHosting — Android Companion App (Final Year Project)

PKHosting is a native Android companion app for the Pakistani web-hosting
provider [pkhosting.com](https://www.pkhosting.com/). Customers can browse
hosting plans, search and register domains, place (demo) orders, pay with a
clearly-labelled demo checkout, track invoices, and raise support tickets —
all backed by Firebase Authentication and Cloud Firestore.

Built with **Java + XML** in Android Studio.

## Tech stack

| Layer | Technology |
|---|---|
| Language | Java 17, XML layouts |
| Min / Target SDK | 23 / 34 (Android 6.0 – 14) |
| UI | Material Components (Material 3), RecyclerView, CardView, SwipeRefreshLayout, ViewBinding |
| Navigation | Bottom navigation + Navigation Drawer + Toolbar |
| Backend | Firebase Authentication (email/password) + Cloud Firestore |
| Build | Gradle 8.2, Android Gradle Plugin 8.2.2 |

## Features

- **Splash screen** with logo and tagline; routes to Login or Dashboard based on auth state
- **Home dashboard** — hero banner, domain search bar, featured plans carousel, quick actions
- **Hosting plans** — the four real PKHosting plans (Personal, Business, Professional, Reseller R2) with PKR prices, renewal prices, feature lists, category filter chips and "Order now"
- **Plan comparison table** — side-by-side feature grid of all plans
- **Domain search & registration** — demo availability check, TLD pricing (.com, .pk, .net, .org, .online), multi-year pricing
- **Domain transfer** — domain + EPP/auth-code form that raises a pending transfer order
- **Register / Login / Forgot password** — Firebase Auth with full input validation
- **Profile** — view and edit name/phone (email is read-only)
- **Customer dashboard** — active services, domains, unpaid invoices, recent orders/domains, pull-to-refresh
- **Orders & history** — every order, newest first
- **Invoices** — list + detail screen, "Pay now" marks the invoice paid and activates its order
- **Payments** — **Demo Mode** checkout (JazzCash / Easypaisa / bank / card) with simulated latency and transaction ids; card fields validated only for the card method
- **Support** — tickets list, new-ticket form, ticket conversation with replies, plus call / email / live-chat options
- **Contact page** — real PK Hosting phone, email and website links
- **FAQs / knowledgebase** — live search + category chips, expandable answers
- **Settings** — notification preference, one-tap sample-data seeder, app info, logout
- **UX quality** — loading / empty / error states on every screen, inline validation, pull-to-refresh, rounded cards, consistent spacing, transitions

### Theme (strict palette)

| Colour | Hex | Use |
|---|---|---|
| Primary Blue | `#0877D1` | Buttons, accents, toolbar |
| Dark Black | `#101820` | Hero/featured cards, status bar |
| White | `#FFFFFF` | Backgrounds, cards |
| Light Blue | `#EAF4FC` | Screen backgrounds, badges |

The pkhosting.com website uses green accents; **every green in this app is
replaced with the brand blue** — no green appears anywhere in the app.

## Folder structure

```
PKHosting/
├── app/src/main/
│   ├── AndroidManifest.xml
│   ├── java/com/pkhosting/app/
│   │   ├── PkApp.java                  # Application class (force light mode)
│   │   ├── activities/                 # 11 screens (Splash, Auth, Main, Detail screens)
│   │   ├── fragments/                  # 13 fragments (tabs, lists, forms)
│   │   ├── adapters/                   # 7 RecyclerView adapters
│   │   ├── models/                     # 9 Firestore POJOs (Plan, Order, Invoice, ...)
│   │   └── utils/                      # Constants, Validators, CurrencyUtil, DateUtils,
│   │                                   # PaymentManager (demo gateway), OrderUtils,
│   │                                   # FirestoreSeeder, StateViews, AuthUtils
│   └── res/
│       ├── layout/                     # 34 layouts (activities, fragments, list items)
│       ├── menu/                       # bottom_nav_menu.xml, drawer_menu.xml
│       ├── drawable/                   # vector icons + shape backgrounds
│       └── values/                     # colors, themes, dimens, strings
├── firestore.rules                     # Security rules (owner-only data)
├── docs/FIREBASE_SETUP.md              # Step-by-step Firebase setup guide
└── docs/SCREENS.md                     # Screen list for FYP documentation
```

## Run instructions

1. **Open in Android Studio** — File ▸ Open ▸ select the `PKHosting` folder.
   Let Gradle sync finish (the wrapper downloads Gradle 8.2 automatically).
2. **Set up Firebase** — follow [docs/FIREBASE_SETUP.md](docs/FIREBASE_SETUP.md).
   In short: create a Firebase project, register the Android app with package
   `com.pkhosting.app`, download the generated `google-services.json` and
   replace the placeholder at `app/google-services.json`, enable
   Email/Password sign-in, create Firestore, and publish `firestore.rules`.
3. **Load sample data** — run the app, register an account, then open
   **Settings ▸ Load sample data**. This seeds the `plans`, `faqs` and
   `takenDomains` collections (only if they are empty).
4. **Run** ▸ `app` on an emulator or device (API 23+).

> The placeholder `google-services.json` lets Gradle sync succeed, but
> Firebase calls will only work after you replace it with your own file.

### Notes for reviewers

- All list queries use a single `whereEqualTo("userId", uid)` filter and are
  **sorted client-side** by `createdAt` — this avoids needing composite
  Firestore indexes, so the app works immediately after publishing the rules.
- Payments are **Demo Mode only**: `PaymentManager.processDemoPayment()`
  simulates a gateway round-trip and returns a `DEMO-XXXXXXXX` transaction id.
  No real keys or credentials exist anywhere in the project. The class
  documents how to plug in JazzCash / Easypaisa / Stripe later via a backend.
- Domain availability is a demo check against the public `takenDomains`
  collection seeded by the app.
