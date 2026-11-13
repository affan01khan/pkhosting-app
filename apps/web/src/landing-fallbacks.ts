import type { LandingFallback } from "./landing";

const modules = import.meta.glob<{ default: LandingFallback }>(
  ["./landing-fallbacks/*.{ts,tsx}", "!./landing-fallbacks/*.test.{ts,tsx}"],
  { eager: true }
);

export const landingFallbacks: readonly LandingFallback[] = Object.entries(
  modules
)
  .toSorted(([left], [right]) => left.localeCompare(right))
  .map(([path, module]) => {
    if (typeof module.default !== "function") {
      throw new TypeError(
        `${path.slice(2)} must default-export a LandingFallback.`
      );
    }

    return module.default;
  });
