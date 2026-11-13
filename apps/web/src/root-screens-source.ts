import type { RootScreens } from "./root-screens";
import { plainRootScreens } from "./root-screens";

const modules = import.meta.glob<{ default: RootScreens }>(
  ["./root-screens/*.{ts,tsx}", "!./root-screens/*.test.{ts,tsx}"],
  { eager: true }
);

const KEYS = {
  resolveLocale: true,
  texts: true,
  MissingChunk: true,
  ErrorScreen: true,
  NotFound: true,
  Starter: true,
} satisfies Record<keyof RootScreens, true>;

const isRootScreens = (value: unknown): value is RootScreens =>
  typeof value === "object" &&
  value !== null &&
  "resolveLocale" in value &&
  typeof value.resolveLocale === "function" &&
  "texts" in value &&
  typeof value.texts === "function";

const contributed = (): RootScreens => {
  const entries = Object.entries(modules);

  if (entries.length > 1) {
    throw new Error(
      `root-screens/ must hold one source module, found ${entries.map(([path]) => path.slice(2)).join(", ")}.`
    );
  }

  const [entry] = entries;

  if (entry === undefined) {
    return plainRootScreens;
  }

  const [path, module] = entry;

  const file = path.slice(2);

  if (!isRootScreens(module.default)) {
    throw new TypeError(`${file} must default-export RootScreens.`);
  }

  const unknown = Object.keys(module.default).find(
    (key) => !Object.hasOwn(KEYS, key)
  );

  if (unknown !== undefined) {
    throw new TypeError(
      `${file} default-exports RootScreens with an unknown key, ${unknown}: update tanstack-start, or remove the key.`
    );
  }

  return module.default;
};

export const contributedRootScreens: RootScreens = contributed();
