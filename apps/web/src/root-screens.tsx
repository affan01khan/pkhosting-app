import { useRouteContext } from "@tanstack/react-router";
import { createContext, useContext } from "react";
import type { ComponentType, ReactElement } from "react";

export interface RootTexts {
  readonly lang: string;
  readonly dir: "ltr" | "rtl";
  readonly missingChunkTitle: string;
  readonly missingChunk: string;
  readonly reload: string;
  readonly errorTitle: string;
  readonly errorDescription: string;
  readonly retry: string;
  readonly errorDetails: string;
  readonly notFound: string;
  readonly notFoundDescription: string;
  readonly home: string;
  readonly starterEyebrow: string;
  readonly starterHint: (paths: {
    readonly file: ReactElement;
    readonly directory: ReactElement;
  }) => ReactElement;
}

export interface MissingChunkScreenProps {
  readonly texts: RootTexts;
  readonly onReload: () => void;
}

export interface ErrorScreenProps {
  readonly texts: RootTexts;
  readonly message: string;
  readonly onRetry: () => void;
}

export interface NotFoundScreenProps {
  readonly texts: RootTexts;
}

export interface StarterScreenProps {
  readonly texts: RootTexts;
  readonly appName: string;
  readonly paths: { readonly file: string; readonly directory: string };
}

export interface RootScreens {
  readonly resolveLocale: () => string;
  readonly texts: (locale: string) => RootTexts;
  readonly MissingChunk?: ComponentType<MissingChunkScreenProps>;
  readonly ErrorScreen?: ComponentType<ErrorScreenProps>;
  readonly NotFound?: ComponentType<NotFoundScreenProps>;
  readonly Starter?: ComponentType<StarterScreenProps>;
}

const ENGLISH: RootTexts = {
  lang: "en",
  dir: "ltr",
  missingChunkTitle: "This app was updated",
  missingChunk:
    "This page could not load, likely because the app was updated. Reload to get the latest version.",
  reload: "Reload",
  errorTitle: "Something went wrong",
  errorDescription:
    "This page ran into an unexpected problem. Try again, or come back in a moment.",
  retry: "Try again",
  errorDetails: "Details",
  notFound: "Page not found",
  notFoundDescription: "Nothing lives at this address.",
  home: "Go to the home page",
  starterEyebrow: "Getting started",
  starterHint: ({ file, directory }) => (
    <>
      Edit {file}, or add a route under {directory}.
    </>
  ),
};

export const plainRootScreens: RootScreens = {
  resolveLocale: () => "en",
  texts: () => ENGLISH,
};

export const RootScreensContext = createContext<RootScreens>(plainRootScreens);

export const useRootScreens = () => {
  const screens = useContext(RootScreensContext);

  const locale = useRouteContext({
    from: "__root__",
    select: (context): string | undefined => context.locale,
  });

  return { screens, texts: screens.texts(locale ?? "en") };
};

export const useRootTexts = (): RootTexts => useRootScreens().texts;
