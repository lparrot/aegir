import pinia from "@/stores";
import { createApp } from "vue";

import App from "./App.vue";
import "./assets/index.scss";

import AegirPlugin from "./plugins/aegir";
import router from "./router";

const main = async () => {
  const app = createApp(App);

  app.config.globalProperties.$log = (data: any) => {
    console.log(data);
  };

  app
    .use(pinia)
    .use(router)
    .use(AegirPlugin);

  return app;
};

main().then(async _root => {
  _root.mount("#app");
});
