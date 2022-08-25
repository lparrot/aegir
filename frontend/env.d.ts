/// <reference types="vite/client" />

interface ImportMetaEnv {
  readonly APP_DEV_PORT: number;
  readonly APP_DEV_PROXY: string;
}

interface ImportMeta {
  readonly env: ImportMetaEnv;
}
