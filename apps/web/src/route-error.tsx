import { useRouter } from "@tanstack/react-router";
import type { ErrorComponentProps } from "@tanstack/react-router";
import { useEffect } from "react";

import { useRootScreens } from "./root-screens";
import type { ErrorScreenProps, MissingChunkScreenProps } from "./root-screens";

const MISSING_CHUNK_MESSAGES = [
  "Failed to fetch dynamically imported module",
  "error loading dynamically imported module",
  "Importing a module script failed",
  "Unable to preload CSS",
];

const isMissingChunkError = (error: Error): boolean =>
  MISSING_CHUNK_MESSAGES.some((message) => error.message.startsWith(message));

export const page = {
  reload: () => {
    window.location.reload();
  },
};

const reloadOnce = (error: Error) => {
  const key = `tanstack_router_reload:${error.message}`;

  if (!window.navigator.onLine) {
    return;
  }

  try {
    if (window.sessionStorage.getItem(key) !== null) {
      return;
    }

    window.sessionStorage.setItem(key, "1");
  } catch {
    return;
  }

  page.reload();
};

const PlainMissingChunk = ({ texts, onReload }: MissingChunkScreenProps) => (
  <div className="flex flex-col items-start gap-4 p-8">
    <h1 className="font-semibold">{texts.missingChunkTitle}</h1>
    <p>{texts.missingChunk}</p>
    <button
      type="button"
      className="rounded-md border px-3 py-1.5 text-sm"
      onClick={onReload}
    >
      {texts.reload}
    </button>
  </div>
);

const MissingChunk = ({ error }: { error: Error }) => {
  const { screens, texts } = useRootScreens();
  const Screen = screens.MissingChunk ?? PlainMissingChunk;

  useEffect(() => {
    reloadOnce(error);
  }, [error]);

  return (
    <Screen
      texts={texts}
      onReload={() => {
        page.reload();
      }}
    />
  );
};

const PlainError = ({ texts, message, onRetry }: ErrorScreenProps) => (
  <div className="flex flex-col items-start gap-2 p-8">
    <h1 className="font-semibold">{texts.errorTitle}</h1>
    <p>{texts.errorDescription}</p>
    <button
      type="button"
      className="rounded-md border px-3 py-1.5 text-sm"
      onClick={onRetry}
    >
      {texts.retry}
    </button>
    <details>
      <summary>{texts.errorDetails}</summary>
      <pre className="text-sm whitespace-pre-wrap" dir="auto">
        {message}
      </pre>
    </details>
  </div>
);

const OtherError = ({ error }: Pick<ErrorComponentProps, "error">) => {
  const { screens, texts } = useRootScreens();
  const router = useRouter();
  const Screen = screens.ErrorScreen ?? PlainError;
  const thrown: unknown = error;
  const message = thrown instanceof Error ? thrown.message : String(thrown);

  return (
    <Screen
      texts={texts}
      message={message}
      onRetry={() => {
        void router.invalidate();
      }}
    />
  );
};

export const RouteError = ({ error }: ErrorComponentProps) =>
  error instanceof Error && isMissingChunkError(error) ? (
    <MissingChunk error={error} />
  ) : (
    <OtherError error={error} />
  );
