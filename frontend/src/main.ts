import { createApp } from "vue";
import AegirPlugin from "./plugins/aegir";

import App from "./App.vue";
import router from "./router";

import "./assets/index.scss";
import pinia from "@/stores";

const app = createApp(App);

app.config.globalProperties.$log = (data: any) => {
  console.log(data);
};

app.use(pinia);

app.use(router);

app.use(AegirPlugin);

app.mount("#app");
