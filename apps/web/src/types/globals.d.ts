declare const process: { readonly env: Record<string, string | undefined> };

interface ImportMetaEnv {
  readonly APP_NAME?: string;
}
