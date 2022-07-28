import { boot } from "quasar/wrappers";
import axios, { AxiosInstance, AxiosResponse } from "axios";
import { Notify } from "quasar";
import { useAuthStore } from "stores/auth";
import { CustomAxiosInstance } from "src/types";
import useAppLocalStorage from "src/composables/useAppLocalStorage";

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
  app.config.errorHandler = (err, instance, info) => {
    console.error(err);
  };

  const { storageToken, storageCurrentRoute } = useAppLocalStorage();

  app.config.globalProperties.$axios = axios;
  app.config.globalProperties.$api = api;

  api.defaults.timeout = 2000;
  api.defaults.timeoutErrorMessage = "timeout";

  api.interceptors.request.use((config) => {
    if (storageToken.value != null) {
      config.headers.common["Authorization"] = `Bearer ${storageToken.value}`;
    }

    return config;
  });

  api.interceptors.response.use((response) => {
    const data = response.data;

    if (urlPath.includes("errors/bad-gateway")) {
      if (storageCurrentRoute.value != null) {
        redirect(storageCurrentRoute.value);
      } else {
        redirect({ name: "index" });
      }
    }

    if (data?.type === "message") {
      Notify.create({
        message: data.error,
        color: data.color ?? "negative",
        textColor: data.textColor ?? "white",
      });
    }

    return response;
  }, async function(error) {

    if (error.code === "ECONNABORTED") {
      await router.push({ name: "errors-502" });
      return;
    }

    const response: AxiosResponse = error.response;

    let authStore = useAuthStore();

    switch (response.status) {
      case 400:
        if (response.data?.tag === "jwt") {
          await authStore.disconnect();
        }
        break;
      case 401:
        Notify.create({
          message: `Vous n'etes pas connecté`,
          color: "negative",
        });

        await authStore.disconnect();
        break;
      case 404:
        Notify.create({
          message: "Erreur: " + response.data.message,
          color: "negative",
        });
        break;
      default:
        break;
    }

    return Promise.reject(error);
  });
});

export { api };
