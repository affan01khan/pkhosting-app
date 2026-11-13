import { useRouteContext } from "@tanstack/react-router";
import { createIsomorphicFn } from "@tanstack/react-start";

export const DEFAULT_APP_NAME = "App";

const BUILT_APP_NAME =
  import.meta.env.APP_NAME === undefined || import.meta.env.APP_NAME === ""
    ? DEFAULT_APP_NAME
    : import.meta.env.APP_NAME;

export const resolveAppName = createIsomorphicFn()
  .server(() =>
    process.env.APP_NAME === undefined || process.env.APP_NAME === ""
      ? BUILT_APP_NAME
      : process.env.APP_NAME
  )
  .client(() => BUILT_APP_NAME);

export const useAppName = (): string =>
  useRouteContext({
    from: "__root__",
    select: (context) => context.appName,
  }) ?? BUILT_APP_NAME;
