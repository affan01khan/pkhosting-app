import type { QueryClient } from "@tanstack/react-query";
import {
  HeadContent,
  Scripts,
  createRootRouteWithContext,
} from "@tanstack/react-router";
import type { ReactNode } from "react";

import { DEFAULT_APP_NAME } from "../app-name";
import { useRootTexts } from "../root-screens";
import { contributedRootScreens } from "../root-screens-source";

import appCss from "../index.css?url";

export interface RouterContext {
  queryClient: QueryClient;
  landingPath: () => string | undefined;
  appName?: string;
}

const RootDocument = ({ children }: { children: ReactNode }) => {
  const { lang, dir } = useRootTexts();

  return (
    <html lang={lang} dir={dir} suppressHydrationWarning>
      <head>
        <HeadContent />
      </head>
      <body>
        {children}
        <Scripts />
      </body>
    </html>
  );
};

export const Route = createRootRouteWithContext<RouterContext>()({
  beforeLoad: () => ({ locale: contributedRootScreens.resolveLocale() }),
  head: ({ match }) => ({
    meta: [
      { charSet: "utf-8" },
      { name: "viewport", content: "width=device-width, initial-scale=1" },
      { title: match.context.appName ?? DEFAULT_APP_NAME },
      {
        name: "description",
        content:
          "Compare PK Hosting web hosting plans, domain prices and support in Pakistan, then check out on pkhosting.com.",
      },
    ],
    links: [
      { rel: "stylesheet", href: appCss },
      { rel: "icon", type: "image/svg+xml", href: "/favicon.svg" },
    ],
  }),
  shellComponent: RootDocument,
});
