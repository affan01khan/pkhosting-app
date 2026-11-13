import { createServer } from "vite";

const server = await createServer({
  clearScreen: false,
  logLevel: "warn",
  server: { hmr: false, middlewareMode: true, watch: null },
});

await server.close();

process.exit(0);
