import { fileURLToPath, URL } from "node:url";

import { defineConfig, loadEnv } from "vite";
import vue from "@vitejs/plugin-vue";

// https://vitejs.dev/config/

// @ts-ignore
export default defineConfig(({ mode }) => {
  // Load env file based on `mode` in the current working directory.
  // Set the third parameter to '' to load all env regardless of the `VITE_` prefix.
  const env = loadEnv(mode, process.cwd(), "");

  return {
    plugins: [
      vue(),
    ],

    envPrefix: "APP_",

    resolve: {
      alias: {
        "@": fileURLToPath(new URL("./src", import.meta.url)),
        "back_types": fileURLToPath(new URL("./.generated/rest.ts", import.meta.url)),
      },
    },

    server: {
      port: env.APP_DEV_PORT,
      proxy: {
        "/api": env.APP_DEV_PROXY,
        "/swagger-ui": env.APP_DEV_PROXY,
        "/v3/api-docs": env.APP_DEV_PROXY,
        "/ws": {
          target: env.APP_DEV_PROXY,
          ws: true,
        },
      },
    },
  };
});
