import { existsSync, readdirSync } from "node:fs";
import { fileURLToPath } from "node:url";

import tailwindcss from "@tailwindcss/vite";
import { tanstackStart } from "@tanstack/react-start/plugin/vite";
import viteReact from "@vitejs/plugin-react";
import { nitro } from "nitro/vite";
import { defineConfig, loadEnv, runnerImport } from "vite";
import type { Plugin } from "vite";

const PLUGINS_DIR = fileURLToPath(new URL("vite-plugins/", import.meta.url));

const WORKSPACE_ROOT = fileURLToPath(new URL("../../", import.meta.url));

const contributedPlugins = async (): Promise<Plugin[]> => {
  if (!existsSync(PLUGINS_DIR)) {
    return [];
  }

  const files = readdirSync(PLUGINS_DIR)
    .filter(
      (file) =>
        /\.[cm]?[jt]s$/u.test(file) && !/\.(?:test|d)\.[cm]?[jt]s$/u.test(file)
    )
    .toSorted();

  const contributed = await Promise.all(
    files.map(async (file) => {
      const { module } = await runnerImport<{ default?: Plugin | Plugin[] }>(
        `${PLUGINS_DIR}${file}`
      );

      const plugins = [module.default ?? []].flat();

      if (
        plugins.length === 0 ||
        plugins.some(
          (plugin) => plugin.name === undefined || plugin.name === ""
        )
      ) {
        throw new Error(
          `vite-plugins/${file} must default-export a Vite plugin, or an array of them.`
        );
      }

      return plugins;
    })
  );

  return contributed.flat();
};

export default defineConfig(async ({ mode }) => ({
  define: {
    "import.meta.env.APP_NAME": JSON.stringify(
      loadEnv(mode, WORKSPACE_ROOT, "APP_NAME").APP_NAME ?? ""
    ),
  },
  server: {
    port: 3001,
  },
  resolve: {
    tsconfigPaths: true,
  },
  plugins: [
    tailwindcss(),
    tanstackStart(),
    nitro({
      compressPublicAssets: { brotli: true, gzip: true },
    }),
    viteReact({ compiler: true }),
    ...(await contributedPlugins()),
  ],
}));
