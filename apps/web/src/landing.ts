import { resolvePath } from "@tanstack/react-router";
import type {
  AnyRouter,
  StaticDataRouteOption,
  TrailingSlashOption,
} from "@tanstack/react-router";

declare module "@tanstack/react-router" {
  interface StaticDataRouteOption {
    landing?: boolean | number;
  }
}

export interface LandingRoute {
  id: string;
  fullPath: string;
  options: { staticData?: StaticDataRouteOption };
}

export type LandingFallback = (
  routes: readonly LandingRoute[],
  canLand: (route: LandingRoute) => boolean
) => LandingRoute | undefined;

const canLand = (route: LandingRoute): boolean =>
  route.options.staticData?.landing !== false;

const urlOf = (
  router: { options: { trailingSlash?: TrailingSlashOption } },
  route: LandingRoute
): string => resolvePath("/", route.fullPath, router.options.trailingSlash);

export const landingPath = (
  router: AnyRouter,
  fallbacks: readonly LandingFallback[] = []
): string | undefined => {
  const routes = Object.values<LandingRoute>(router.routesById);
  let best: { rank: number; route: LandingRoute } | undefined;

  for (const route of routes) {
    const claim = route.options.staticData?.landing;

    if (claim === undefined || claim === false) {
      continue;
    }

    const rank = claim === true ? 0 : claim;

    if (Number.isNaN(rank)) {
      continue;
    }

    if (
      !best ||
      rank > best.rank ||
      (rank === best.rank && route.id < best.route.id)
    ) {
      best = { rank, route };
    }
  }

  if (best) {
    return urlOf(router, best.route);
  }

  for (const fallback of fallbacks) {
    const route = fallback(routes, canLand);

    if (route !== undefined && canLand(route)) {
      return urlOf(router, route);
    }
  }

  return undefined;
};
