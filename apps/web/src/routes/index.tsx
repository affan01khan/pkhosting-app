import { createFileRoute } from "@tanstack/react-router";

const SITE = "https://www.pkhosting.com";

const NAV_LINKS = [
  { label: "Plans", href: "#plans" },
  { label: "Domains", href: "#domains" },
  { label: "Features", href: "#features" },
  { label: "Status", href: `${SITE}/status/` },
  { label: "Contact", href: `${SITE}/contact/` },
];

const DOMAINS = [
  { tld: ".com", price: "Rs 3,299", period: "first year", was: "Rs 3,762" },
  { tld: ".pk", price: "Rs 3,899", period: "2 years", was: "Rs 5,460" },
  { tld: ".com.pk", price: "Rs 3,899", period: "2 years", was: "Rs 5,460" },
  { tld: ".org.pk", price: "Rs 3,899", period: "2 years", was: "Rs 5,460" },
  { tld: ".net.pk", price: "Rs 3,899", period: "2 years", was: "Rs 5,460" },
];

const FEATURES = [
  {
    title: "Fast SSD Storage",
    body: "Shared hosting runs on SSD storage with a tuned managed stack to keep your sites responsive.",
    icon: "M3 7a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2v10a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V7Zm4 2h.01M7 15h.01",
  },
  {
    title: "SSL & Sensible Security",
    body: "Hosting plans include SSL support, secure defaults and practical guidance for keeping your site protected.",
    icon: "M12 3 4 6v6c0 4.5 3.4 8.2 8 9 4.6-.8 8-4.5 8-9V6l-8-3Zm-2.5 9 2 2 3.5-4",
  },
  {
    title: "Real Human Support",
    body: "Reach a local team by phone, email and dashboard tickets. Live chat and the direct line run Mon\u2013Fri, 9am\u20136pm PKT; tickets are answered every day.",
    icon: "M3 5h18v12H8l-5 4V5Zm5 5h8M8 13h5",
  },
  {
    title: "Daily Backups",
    body: "Daily copies of files, databases and email, kept on the hosting server. Restores from copies still listed in the dashboard are included \u2014 best-effort, with no recovery-time SLA.",
    icon: "M4 7c0-1.7 3.6-3 8-3s8 1.3 8 3-3.6 3-8 3-8-1.3-8-3Zm0 0v10c0 1.7 3.6 3 8 3s8-1.3 8-3V7M4 12c0 1.7 3.6 3 8 3s8-1.3 8-3",
  },
  {
    title: "Assisted Website Migration",
    body: "Already hosted elsewhere? The team helps move eligible websites, email and databases with a clear plan.",
    icon: "M4 8h12l-3-3m3 3-3 3M20 16H8l3-3m-3 3 3 3",
  },
  {
    title: "1-Click WordPress",
    body: "Launch WordPress quickly with managed setup help and migration support.",
    icon: "M12 21a9 9 0 1 1 0-18 9 9 0 0 1 0 18Zm-4-13 2 9 2-6 2 6 2-9",
  },
];

const PLANS = [
  {
    name: "Personal Hosting",
    audience: "For students, hobbyists & individuals",
    badge: "Black Friday \u00b7 36% off",
    tag: null,
    price: "Rs 4,490",
    period: "/yr",
    renewal:
      "First year Rs 4,490 \u00b7 Renews at Rs 6,990 for the same term. Add-ons and taxes extra.",
    featured: false,
    features: [
      "1 Website",
      "5 GB SSD Storage",
      "Unmetered bandwidth \u2014 fair-use policy applies",
      "5 Email Accounts",
      "Free .com with a 3-year term",
      "Free SSL Certificate",
      "Daily backups \u00b7 1-day retention",
      "WordPress Setup Help",
      "PK Hosting Dashboard",
      "Email + Database Support",
    ],
  },
  {
    name: "Business Hosting",
    audience: "For growing businesses & teams",
    badge: "Black Friday \u00b7 40% off",
    tag: "Most Popular",
    price: "Rs 11,990",
    period: "/yr",
    renewal:
      "First year Rs 11,990 \u00b7 Renews at Rs 19,990 for the same term. Add-ons and taxes extra.",
    featured: true,
    features: [
      "5 Websites",
      "25 GB SSD Storage",
      "Unmetered bandwidth \u2014 fair-use policy applies",
      "25 Email Accounts",
      "Free .com with a 3-year term",
      "Free SSL Certificate",
      "Daily backups \u00b7 3-day retention",
      "Priority Setup",
      "Managed DNS Help",
      "Business Email Support",
    ],
  },
  {
    name: "Professional Hosting",
    audience: "For software houses & corporates",
    badge: "Black Friday \u00b7 40% off",
    tag: "Best Value",
    price: "Rs 23,990",
    period: "/yr",
    renewal:
      "First year Rs 23,990 \u00b7 Renews at Rs 39,990 for the same term. Add-ons and taxes extra.",
    featured: false,
    features: [
      "10 Websites",
      "50 GB SSD Storage",
      "Unmetered bandwidth \u2014 fair-use policy applies",
      "50 Email Accounts",
      "Free .com with a 3-year term",
      "Free SSL Certificate",
      "Daily backups \u00b7 5-day retention",
      "No fixed LVE allocation \u2014 fair-use CPU, RAM and I/O",
      "Priority Support",
      "Developer-Friendly Setup",
    ],
  },
];

const PROMISES = [
  {
    title: "Availability-Focused Setup",
    body: "The hosting stack is matched to the workload so your site has a practical reliability path.",
  },
  {
    title: "We Treat Your Site as Ours",
    body: "Ordering, provisioning and support stay hands-on, with real people accountable for each customer setup.",
  },
  {
    title: "Easy Hosting Control",
    body: "Manage websites, email, files and databases from the PK Hosting dashboard.",
  },
  {
    title: "Clear Billing Rules",
    body: "Pricing, renewal terms and refund limits are published before checkout so there are no surprises later.",
  },
];

const STATS = [
  { value: "2010", label: "Hosting experience since 2010" },
  { value: "PK", label: "Pakistan-based hosting support" },
  { value: "1-Click", label: "WordPress setup" },
  { value: "Daily", label: "Tickets answered every day" },
];

const SERVICES = [
  {
    title: "SSL Certificates",
    body: "Secure your visitors and boost trust with DV, OV and EV SSL certificates, installed for you.",
  },
  {
    title: "Web Development",
    body: "Custom websites, WordPress themes and online stores designed to convert visitors into customers.",
  },
  {
    title: "Website Growth",
    body: "Improve content, structure and conversion paths so more visitors become customers.",
  },
  {
    title: "Site Management",
    body: "Updates, speed tuning, security hardening and content \u2014 let the team manage your site end to end.",
  },
];

const NETWORK_SITES = [
  "TECHi.com",
  "WPArena.com",
  "TechEngage.com",
  "Medic.pk",
  "MetalSite.com",
  "AccountStop.com",
  "aiTransform.com",
];

const CHECKS = [
  {
    title: "Current service checks",
    body: "The status page checks selected public services and shows when they were last checked. It does not measure historical uptime or every hosting node.",
    link: "View system status",
    href: `${SITE}/status/`,
  },
  {
    title: "Published backup terms",
    body: "Each plan's stated backup frequency, retention, on-server location and best-effort restore terms are published. This policy is not proof that a specific restore will succeed.",
    link: "Read the backup policy",
    href: `${SITE}/backup-policy/`,
  },
  {
    title: "Published support channels",
    body: "The contact page lists phone, email, ticket options and support hours. No measured response-time claim is published.",
    link: "See contact details",
    href: `${SITE}/contact/`,
  },
];

const Icon = ({ path }: { path: string }) => (
  <svg
    viewBox="0 0 24 24"
    fill="none"
    stroke="currentColor"
    strokeWidth="1.8"
    strokeLinecap="round"
    strokeLinejoin="round"
    aria-hidden="true"
    className="h-6 w-6"
  >
    <path d={path} />
  </svg>
);

const CheckIcon = () => (
  <svg
    viewBox="0 0 24 24"
    fill="none"
    stroke="currentColor"
    strokeWidth="2.4"
    strokeLinecap="round"
    strokeLinejoin="round"
    aria-hidden="true"
    className="mt-1 h-4 w-4 shrink-0 text-emerald-500"
  >
    <path d="m5 13 4 4L19 7" />
  </svg>
);

const SectionHeading = ({
  eyebrow,
  title,
  dark = false,
}: {
  eyebrow: string;
  title: string;
  dark?: boolean;
}) => (
  <div className="mx-auto max-w-2xl text-center">
    <p className="text-sm font-semibold tracking-wide text-emerald-500 uppercase">
      {eyebrow}
    </p>
    <h2
      className={`mt-2 text-3xl font-bold tracking-tight sm:text-4xl ${
        dark ? "text-white" : "text-slate-900"
      }`}
    >
      {title}
    </h2>
  </div>
);

const Logo = () => (
  <a href="#top" className="flex items-center gap-2.5">
    <span className="flex h-9 w-9 items-center justify-center rounded-lg bg-emerald-500 text-sm font-black text-slate-950">
      PK
    </span>
    <span className="text-lg font-bold tracking-tight text-white">
      PK Hosting
    </span>
  </a>
);

const LandingPage = () => (
  <div id="top" className="min-h-full bg-white">
    <div className="block bg-gradient-to-r from-emerald-600 to-emerald-500 px-4 py-2 text-center text-sm font-medium text-white">
      <span className="font-bold">BLACK FRIDAY SALE</span> Hosting from Rs 4,490
      for the first year &middot; .pk family Rs 3,899/2yr &middot; .com Rs
      3,299/first year &middot;{" "}
      <a
        href={`${SITE}/`}
        target="_blank"
        rel="noopener noreferrer"
        className="underline underline-offset-2"
      >
        See all deals &rarr;
      </a>
    </div>

    <header className="sticky top-0 z-40 border-b border-white/10 bg-slate-950/95 backdrop-blur">
      <div className="mx-auto flex h-16 max-w-7xl items-center justify-between gap-4 px-4 sm:px-6 lg:px-8">
        <Logo />
        <nav className="hidden items-center gap-6 md:flex" aria-label="Main">
          {NAV_LINKS.map((link) =>
            link.href.startsWith("#") ? (
              <a
                key={link.label}
                href={link.href}
                className="text-sm font-medium text-slate-300 transition hover:text-white"
              >
                {link.label}
              </a>
            ) : (
              <a
                key={link.label}
                href={link.href}
                target="_blank"
                rel="noopener noreferrer"
                className="text-sm font-medium text-slate-300 transition hover:text-white"
              >
                {link.label}
              </a>
            )
          )}
        </nav>
        <div className="flex items-center gap-3">
          <a
            href="tel:+924235692952"
            className="hidden text-sm font-medium text-slate-300 transition hover:text-white lg:block"
          >
            (042) 35692952
          </a>
          <a
            href={`${SITE}/`}
            target="_blank"
            rel="noopener noreferrer"
            className="rounded-lg bg-emerald-500 px-4 py-2 text-sm font-semibold text-slate-950 transition hover:bg-emerald-400"
          >
            Client login
          </a>
        </div>
      </div>
    </header>

    <main>
      <section className="relative overflow-hidden bg-slate-950">
        <div
          aria-hidden="true"
          className="pointer-events-none absolute -top-40 left-1/2 h-[36rem] w-[72rem] -translate-x-1/2 rounded-full bg-emerald-500/15 blur-3xl"
        />
        <div className="relative mx-auto max-w-7xl px-4 py-20 sm:px-6 sm:py-28 lg:px-8">
          <div className="mx-auto max-w-3xl text-center">
            <p className="inline-flex items-center gap-2 rounded-full border border-emerald-500/30 bg-emerald-500/10 px-4 py-1.5 text-sm font-medium text-emerald-300">
              16+ Years &middot; Pakistan Hosting Support
            </p>
            <h1 className="mt-6 text-4xl font-black tracking-tight text-white sm:text-5xl lg:text-6xl">
              Web Hosting in Pakistan with{" "}
              <span className="text-emerald-400">Local Support</span>
            </h1>
            <p className="mt-4 text-xl font-medium text-emerald-300/90">
              We treat your website as our own.
            </p>
            <p className="mt-4 text-lg leading-relaxed text-slate-300">
              Practical web hosting services. Compare hosting, domains, VPS and
              dedicated server options, then complete checkout and billing on
              the main site.
            </p>
            <div className="mt-8 flex flex-col items-center justify-center gap-3 sm:flex-row">
              <a
                href="#plans"
                className="w-full rounded-xl bg-emerald-500 px-8 py-3.5 text-base font-semibold text-slate-950 transition hover:bg-emerald-400 sm:w-auto"
              >
                View Pricing
              </a>
              <a
                href={`${SITE}/`}
                target="_blank"
                rel="noopener noreferrer"
                className="w-full rounded-xl border border-white/20 px-8 py-3.5 text-base font-semibold text-white transition hover:border-emerald-400 hover:text-emerald-300 sm:w-auto"
              >
                Explore Servers
              </a>
            </div>
            <ul className="mt-8 flex flex-wrap items-center justify-center gap-x-8 gap-y-3 text-sm text-slate-300">
              <li className="flex items-center gap-2">
                <CheckIcon /> Trusted locally: 16+ years hosting Pakistani
                websites
              </li>
              <li className="flex items-center gap-2">
                <CheckIcon /> Free SSL on every plan
              </li>
              <li className="flex items-center gap-2">
                <CheckIcon /> Starting at Rs 449/mo &middot; Black Friday first
                term
              </li>
            </ul>
          </div>
        </div>
      </section>

      <section id="domains" className="bg-slate-50 py-16 sm:py-20">
        <div className="mx-auto max-w-7xl px-4 sm:px-6 lg:px-8">
          <SectionHeading
            eyebrow="Live domain availability"
            title="Find your domain"
          />
          <p className="mx-auto mt-3 max-w-xl text-center text-slate-600">
            Fast checkout with live availability search on the main site. Black
            Friday pricing shown below.
          </p>
          <div className="mt-10 grid grid-cols-2 gap-4 sm:grid-cols-3 lg:grid-cols-5">
            {DOMAINS.map((domain) => (
              <a
                key={domain.tld}
                href={`${SITE}/`}
                target="_blank"
                rel="noopener noreferrer"
                className="group rounded-2xl border border-slate-200 bg-white p-5 text-center shadow-sm transition hover:-translate-y-0.5 hover:border-emerald-400 hover:shadow-md"
              >
                <p className="text-xl font-bold text-slate-900 group-hover:text-emerald-600">
                  {domain.tld}
                </p>
                <p className="mt-2 text-lg font-semibold text-emerald-600">
                  {domain.price}
                </p>
                <p className="text-xs text-slate-500">/{domain.period}</p>
                <p className="mt-1 text-xs text-slate-400">
                  was <s>{domain.was}</s>
                </p>
              </a>
            ))}
          </div>
          <div className="mt-8 flex flex-wrap items-center justify-center gap-x-8 gap-y-2 text-sm font-medium text-slate-600">
            <span>Managed Setup</span>
            <span>Secure &amp; Safe</span>
            <span>Daily Ticket Support</span>
          </div>
        </div>
      </section>

      <section id="features" className="bg-white py-16 sm:py-24">
        <div className="mx-auto max-w-7xl px-4 sm:px-6 lg:px-8">
          <SectionHeading
            eyebrow="Why PK Hosting"
            title="Everything you need to succeed online"
          />
          <p className="mx-auto mt-3 max-w-xl text-center text-slate-600">
            Clear plan information, practical hosting support and a clean client
            dashboard.
          </p>
          <div className="mt-12 grid gap-6 sm:grid-cols-2 lg:grid-cols-3">
            {FEATURES.map((feature) => (
              <div
                key={feature.title}
                className="rounded-2xl border border-slate-200 bg-white p-6 shadow-sm transition hover:shadow-md"
              >
                <span className="flex h-12 w-12 items-center justify-center rounded-xl bg-emerald-500/10 text-emerald-600">
                  <Icon path={feature.icon} />
                </span>
                <h3 className="mt-4 text-lg font-semibold text-slate-900">
                  {feature.title}
                </h3>
                <p className="mt-2 text-sm leading-relaxed text-slate-600">
                  {feature.body}
                </p>
              </div>
            ))}
          </div>
        </div>
      </section>

      <section id="plans" className="bg-slate-950 py-16 sm:py-24">
        <div className="mx-auto max-w-7xl px-4 sm:px-6 lg:px-8">
          <SectionHeading
            eyebrow="Web Hosting Plans"
            title="Pick a plan that grows with you"
            dark
          />
          <p className="mx-auto mt-3 max-w-xl text-center text-slate-400">
            Prices update automatically from PK Hosting. Switch currency anytime
            in the top bar, then use Order Now to start checkout on the main
            site.
          </p>
          <div className="mt-12 grid gap-6 lg:grid-cols-3">
            {PLANS.map((plan) => (
              <div
                key={plan.name}
                className={`relative flex flex-col rounded-2xl p-8 ${
                  plan.featured
                    ? "border-2 border-emerald-500 bg-slate-900 shadow-[0_0_40px_rgba(16,185,129,0.15)]"
                    : "border border-white/10 bg-slate-900/60"
                }`}
              >
                {plan.tag && (
                  <span className="absolute -top-3.5 left-1/2 -translate-x-1/2 rounded-full bg-emerald-500 px-4 py-1 text-xs font-bold tracking-wide text-slate-950 uppercase">
                    {plan.tag}
                  </span>
                )}
                <p className="text-sm font-medium text-slate-400">
                  {plan.audience}
                </p>
                <h3 className="mt-1 text-2xl font-bold text-white">
                  {plan.name}
                </h3>
                <span className="mt-3 inline-flex w-fit rounded-full border border-emerald-500/30 bg-emerald-500/10 px-3 py-1 text-xs font-semibold text-emerald-300">
                  {plan.badge}
                </span>
                <p className="mt-5 flex items-baseline gap-1">
                  <span className="text-4xl font-black text-white">
                    {plan.price}
                  </span>
                  <span className="text-lg font-medium text-slate-400">
                    {plan.period}
                  </span>
                </p>
                <p className="mt-2 text-xs leading-relaxed text-slate-400">
                  {plan.renewal}
                </p>
                <a
                  href={`${SITE}/`}
                  target="_blank"
                  rel="noopener noreferrer"
                  className={`mt-6 rounded-xl px-6 py-3 text-center text-sm font-semibold transition ${
                    plan.featured
                      ? "bg-emerald-500 text-slate-950 hover:bg-emerald-400"
                      : "border border-white/20 text-white hover:border-emerald-400 hover:text-emerald-300"
                  }`}
                >
                  Order Now
                </a>
                <ul className="mt-6 space-y-3 border-t border-white/10 pt-6">
                  {plan.features.map((feature) => (
                    <li
                      key={feature}
                      className="flex items-start gap-2.5 text-sm text-slate-300"
                    >
                      <CheckIcon /> {feature}
                    </li>
                  ))}
                </ul>
              </div>
            ))}
          </div>
          <p className="mt-8 text-center text-sm text-slate-400">
            Need something bigger?{" "}
            <a
              href={`${SITE}/`}
              target="_blank"
              rel="noopener noreferrer"
              className="font-semibold text-emerald-400 underline-offset-2 hover:underline"
            >
              Explore VPS and Dedicated Servers
            </a>
            .
          </p>
        </div>
      </section>

      <section className="bg-white py-16 sm:py-24">
        <div className="mx-auto max-w-7xl px-4 sm:px-6 lg:px-8">
          <SectionHeading
            eyebrow="Our Promise"
            title="The host that actually cares about your website"
          />
          <p className="mx-auto mt-3 max-w-xl text-center text-slate-600">
            16 years of web-hosting experience. Compare published plans and
            service terms, then get in touch with questions before ordering.
          </p>
          <div className="mt-12 grid gap-6 sm:grid-cols-2 lg:grid-cols-4">
            {PROMISES.map((promise) => (
              <div
                key={promise.title}
                className="rounded-2xl bg-slate-50 p-6 ring-1 ring-slate-200"
              >
                <h3 className="text-base font-semibold text-slate-900">
                  {promise.title}
                </h3>
                <p className="mt-2 text-sm leading-relaxed text-slate-600">
                  {promise.body}
                </p>
              </div>
            ))}
          </div>
          <dl className="mt-16 grid grid-cols-2 gap-8 text-center lg:grid-cols-4">
            {STATS.map((stat) => (
              <div key={stat.label}>
                <dd className="text-4xl font-black tracking-tight text-emerald-600">
                  {stat.value}
                </dd>
                <dt className="mt-2 text-sm leading-6 text-slate-600">
                  {stat.label}
                </dt>
              </div>
            ))}
          </dl>
        </div>
      </section>

      <section className="bg-slate-50 py-16 sm:py-24">
        <div className="mx-auto max-w-7xl px-4 sm:px-6 lg:px-8">
          <SectionHeading
            eyebrow="Beyond Hosting"
            title="A complete digital home for your business"
          />
          <div className="mt-12 grid gap-6 sm:grid-cols-2 lg:grid-cols-4">
            {SERVICES.map((service) => (
              <a
                key={service.title}
                href={`${SITE}/`}
                target="_blank"
                rel="noopener noreferrer"
                className="group rounded-2xl border border-slate-200 bg-white p-6 shadow-sm transition hover:-translate-y-0.5 hover:border-emerald-400 hover:shadow-md"
              >
                <h3 className="text-base font-semibold text-slate-900 group-hover:text-emerald-600">
                  {service.title}
                </h3>
                <p className="mt-2 text-sm leading-relaxed text-slate-600">
                  {service.body}
                </p>
                <p className="mt-3 text-sm font-semibold text-emerald-600">
                  Learn more &rarr;
                </p>
              </a>
            ))}
          </div>
        </div>
      </section>

      <section className="bg-white py-16 sm:py-24">
        <div className="mx-auto max-w-7xl px-4 sm:px-6 lg:px-8">
          <SectionHeading
            eyebrow="Check our claims"
            title="What you can inspect before ordering"
          />
          <p className="mx-auto mt-3 max-w-xl text-center text-slate-600">
            These public pages explain the service and its limits. They are not
            independent performance audits or customer endorsements.
          </p>
          <div className="mt-12 grid gap-6 lg:grid-cols-3">
            {CHECKS.map((check) => (
              <div
                key={check.title}
                className="flex flex-col rounded-2xl border border-slate-200 p-6"
              >
                <h3 className="text-base font-semibold text-slate-900">
                  {check.title}
                </h3>
                <p className="mt-2 flex-1 text-sm leading-relaxed text-slate-600">
                  {check.body}
                </p>
                <a
                  href={check.href}
                  target="_blank"
                  rel="noopener noreferrer"
                  className="mt-4 text-sm font-semibold text-emerald-600 underline-offset-2 hover:underline"
                >
                  {check.link} &rarr;
                </a>
              </div>
            ))}
          </div>
        </div>
      </section>

      <section className="bg-slate-100 py-12">
        <div className="mx-auto max-w-7xl px-4 sm:px-6 lg:px-8">
          <h2 className="text-center text-sm font-semibold tracking-wide text-slate-500 uppercase">
            TechAbout network sites hosted by PK Hosting
          </h2>
          <ul className="mt-6 flex flex-wrap items-center justify-center gap-x-8 gap-y-2">
            {NETWORK_SITES.map((site) => (
              <li key={site} className="text-sm font-semibold text-slate-700">
                {site}
              </li>
            ))}
          </ul>
          <p className="mx-auto mt-4 max-w-2xl text-center text-xs leading-relaxed text-slate-500">
            These are affiliated TechAbout-network properties, not independent
            customer endorsements or evidence of measured uptime.
          </p>
        </div>
      </section>

      <section className="bg-gradient-to-r from-emerald-600 to-emerald-500 py-16 sm:py-20">
        <div className="mx-auto max-w-4xl px-4 text-center sm:px-6 lg:px-8">
          <h2 className="text-3xl font-black tracking-tight text-white sm:text-4xl">
            Let&rsquo;s get your website online today
          </h2>
          <p className="mt-3 text-lg text-emerald-50">
            Compare plans, complete checkout, view invoices and manage payments
            on pkhosting.com.
          </p>
          <div className="mt-8 flex flex-col items-center justify-center gap-3 sm:flex-row">
            <a
              href="#plans"
              className="w-full rounded-xl bg-slate-950 px-8 py-3.5 text-base font-semibold text-white transition hover:bg-slate-900 sm:w-auto"
            >
              View Pricing
            </a>
            <a
              href="tel:+924235692952"
              className="w-full rounded-xl border-2 border-white/60 px-8 py-3.5 text-base font-semibold text-white transition hover:border-white hover:bg-white/10 sm:w-auto"
            >
              Call (042) 35692952
            </a>
          </div>
        </div>
      </section>
    </main>

    <footer className="bg-slate-950 py-12">
      <div className="mx-auto max-w-7xl px-4 sm:px-6 lg:px-8">
        <div className="flex flex-col items-center justify-between gap-8 md:flex-row md:items-start">
          <div className="max-w-sm text-center md:text-left">
            <Logo />
            <p className="mt-3 text-sm leading-relaxed text-slate-400">
              16+ years of web hosting with local Pakistan support. Lahore,
              Pakistan &middot; Sheridan, USA.
            </p>
          </div>
          <div className="grid grid-cols-2 gap-10 text-center md:text-left">
            <div>
              <h3 className="text-sm font-semibold text-white">Contact</h3>
              <ul className="mt-3 space-y-2 text-sm text-slate-400">
                <li>
                  <a
                    href="tel:+924235692952"
                    className="transition hover:text-emerald-400"
                  >
                    (042) 35692952
                  </a>
                </li>
                <li>
                  <a
                    href="mailto:support@pkhosting.com"
                    className="transition hover:text-emerald-400"
                  >
                    support@pkhosting.com
                  </a>
                </li>
                <li>Mon&ndash;Fri, 9am&ndash;6pm PKT</li>
              </ul>
            </div>
            <div>
              <h3 className="text-sm font-semibold text-white">Links</h3>
              <ul className="mt-3 space-y-2 text-sm text-slate-400">
                <li>
                  <a
                    href={`${SITE}/status/`}
                    target="_blank"
                    rel="noopener noreferrer"
                    className="transition hover:text-emerald-400"
                  >
                    System status
                  </a>
                </li>
                <li>
                  <a
                    href={`${SITE}/backup-policy/`}
                    target="_blank"
                    rel="noopener noreferrer"
                    className="transition hover:text-emerald-400"
                  >
                    Backup policy
                  </a>
                </li>
                <li>
                  <a
                    href={`${SITE}/contact/`}
                    target="_blank"
                    rel="noopener noreferrer"
                    className="transition hover:text-emerald-400"
                  >
                    Contact &amp; support
                  </a>
                </li>
              </ul>
            </div>
          </div>
        </div>
        <p className="mt-10 border-t border-white/10 pt-6 text-center text-xs leading-relaxed text-slate-500">
          Unofficial landing page for pkhosting.com. All plans, prices and terms
          are published by PK Hosting; checkout, billing and account management
          happen on the main site.
        </p>
      </div>
    </footer>
  </div>
);

export const Route = createFileRoute("/")({
  component: LandingPage,
});
