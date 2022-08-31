import pinia from "@/stores";
import { createApp } from "vue";

import App from "./App.vue";

import "./assets/index.scss";
import AegirPlugin from "./plugins/aegir";
import router from "./router";

export const app = createApp(App);

app.config.globalProperties.$log = (data: any) => {
  console.log(data);
};

app.use(pinia);

app.use(router);

app.use(AegirPlugin);

app.mount("#app");
