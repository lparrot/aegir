import { boot } from "quasar/wrappers";
import axios, { AxiosInstance, AxiosResponse } from "axios";
import { LocalStorage, Notify } from "quasar";
import { useAuthStore } from "stores/auth";
import { CustomAxiosInstance } from "src/types";

declare module "@vue/runtime-core" {
  interface ComponentCustomProperties {
    $axios: AxiosInstance;
  }
}

const api: Partial<CustomAxiosInstance> = axios.create();

for (const method of [ "request", "delete", "get", "head", "options", "post", "put", "patch" ]) {
  api["$" + method] = function() {
    return this[method].apply(this, arguments).then(res => res && res.data);
  };
}

export default boot(({ app, router, urlPath, redirect }) => {

  app.config.globalProperties.$axios = axios;
  app.config.globalProperties.$api = api;

  api.defaults.timeout = 2000;
  api.defaults.timeoutErrorMessage = "timeout";

  api.interceptors.request.use(function(config) {
    if (LocalStorage.has("aegir.token")) {
      config.headers.common["Authorization"] = `Bearer ${LocalStorage.getItem("aegir.token")}`;
    }

    return config;
  });

  api.interceptors.response.use(function(response) {
    const data = response.data;

    if (urlPath.includes("errors/no-server")) {
      redirect("/");
    }

    if (data?.type === "error_with_message") {
      Notify.create({
        message: data.message,
        color: data.color ?? "negative",
        textColor: data.textColor ?? "white",
      });
    }

    return response;
  }, async function(error) {

    if (error.code === "ECONNABORTED") {
      await router.push("/errors/no-server");
      return;
    }

    const response: AxiosResponse = error.response;

    let authStore = useAuthStore();

    let error_processed = false;

    switch (response.status) {
      case 401:
        Notify.create({
          message: `Vous n'etes pas connecté`,
          color: "negative",
        });

        await authStore.disconnect();
        error_processed = true;
        break;
      case 404:
        Notify.create({
          message: "Erreur: " + response.data.message,
          color: "negative",
        });
        error_processed = true;
        break;
      default:
        break;
    }

    if (error_processed) {
      return;
    }

    return Promise.reject(error);
  });
});

export { api };
