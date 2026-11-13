import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { createRouter } from "@tanstack/react-router";

import { resolveAppName } from "./app-name";
import { landingPath } from "./landing";
import { landingFallbacks } from "./landing-fallbacks";
import { NotFound } from "./not-found";
import { RootScreensContext } from "./root-screens";
import { contributedRootScreens } from "./root-screens-source";
import { RouteError } from "./route-error";
import type { RouterContext } from "./routes/__root";
import { routeTree } from "./routeTree.gen";
import { scrollRestorationOptions } from "./scroll-root";

export const getRouter = () => {
  const queryClient = new QueryClient({
    defaultOptions: { queries: { staleTime: 60_000 } },
  });

  const context: RouterContext = {
    queryClient,
    landingPath: (): string | undefined =>
      landingPath(router, landingFallbacks),
    appName: resolveAppName(),
  };

  const router = createRouter({
    routeTree,
    context,
    dehydrate: () => ({ appName: context.appName }),
    hydrate: (dehydrated) => {
      context.appName = dehydrated.appName;
    },
    ...scrollRestorationOptions,
    defaultErrorComponent: RouteError,
    defaultNotFoundComponent: NotFound,
    Wrap: ({ children }) => (
      <QueryClientProvider client={queryClient}>
        <RootScreensContext value={contributedRootScreens}>
          {children}
        </RootScreensContext>
      </QueryClientProvider>
    ),
  });

  return router;
};

declare module "@tanstack/react-router" {
  interface Register {
    router: ReturnType<typeof getRouter>;
  }
}
