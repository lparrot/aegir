import vue from "@vitejs/plugin-vue";
import { fileURLToPath, URL } from "node:url";
import AutoImport from "unplugin-auto-import/vite";
import Components from "unplugin-vue-components/vite";
import { defineConfig, loadEnv } from "vite";

// https://vitejs.dev/config/

// @ts-ignore
export default defineConfig(({ mode }) => {
  // Load env file based on `mode` in the current working directory.
  // Set the third parameter to '' to load all env regardless of the `VITE_` prefix.
  const env = loadEnv(mode, process.cwd(), "");

  return {
    plugins: [
      vue(),
      Components({
        dts: "./src/types/auto-components.d.ts",
        dirs: [ "src/components" ],
        types: [
          {
            from: "vue-router",
            names: [ "RouterLink", "RouterView" ],
          },
        ],
      }),
      AutoImport({
        dirs: [
          "./src/**",
        ],
        include: [
          /\.[tj]sx?$/, // .ts, .tsx, .js, .jsx
          /\.vue$/, /\.vue\?vue/, // .vue
          /\.md$/, // .md
        ],
        exclude: [
          "./src/api/**",
        ],
        imports: [
          // presets
          "vue",
          "vue-router",
          "pinia",
          { "@heroicons/vue": [] },
        ],
        dts: "./src/types/auto-imports.d.ts",
      }),
    ],

    envPrefix: "APP_",

    resolve: {
      alias: {
        "@": fileURLToPath(new URL("./src", import.meta.url)),
        "@use": fileURLToPath(new URL("./src/composables", import.meta.url)),
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
