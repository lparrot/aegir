import { store } from "quasar/wrappers";
import { createPinia } from "pinia";
import { markRaw } from "vue";
import { router } from "src/router";

export default store((/* { ssrContext } */) => {
  const pinia = createPinia();

  // You can add Pinia plugins here
  // pinia.use(SomePiniaPlugin)
  pinia.use(({ store }) => {
    store.$router = markRaw(router);
  });

  return pinia;
});
