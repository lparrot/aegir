import { boot } from "quasar/wrappers";
import axios, * as Axios from "axios";
import { AxiosError, AxiosInstance, AxiosResponse } from "axios";
import { Dialog, Notify } from "quasar";
import { useAuthStore } from "stores/auth";
import { CustomAxiosInstance } from "src/types";
import useAppLocalStorage from "src/composables/useAppLocalStorage";
import ErrorDialog from "components/dialogs/ErrorDialog.vue";
import { HttpClient, RestApplicationClient, RestResponse } from "app/.generated/rest";

declare module "@vue/runtime-core" {
  interface ComponentCustomProperties {
    $axios: AxiosInstance;
  }
}

class AxiosHttpClient implements HttpClient {

  constructor(public axios: Partial<CustomAxiosInstance>) {
  }

  request<R>(requestConfig: { method: string; url: string; queryParams?: any; data?: any; copyFn?: (data: R) => R; options?: Axios.AxiosRequestConfig; }): RestResponse<R> {
    function assign(target: any, source?: any) {
      if (source != undefined) {
        for (const key in source) {
          if (source.hasOwnProperty(key)) {
            target[key] = source[key];
          }
        }
      }
      return target;
    }

    const config: Axios.AxiosRequestConfig = {};
    config.method = requestConfig.method as typeof config.method;
    config.url = requestConfig.url;
    config.params = requestConfig.queryParams;
    config.data = requestConfig.data;
    assign(config, requestConfig.options);
    const copyFn = requestConfig.copyFn;

    const axiosResponse = this.axios.$request(config);
    return axiosResponse.then(axiosResponse => {
      if (copyFn && axiosResponse.data) {
        (axiosResponse as any).originalData = axiosResponse.data;
        axiosResponse.data = copyFn(axiosResponse.data);
      }
      return axiosResponse;
    });
  }
}

export class AxiosRestApplicationClient extends RestApplicationClient {

  declare httpClient: AxiosHttpClient;

  constructor(axiosInstance: Partial<CustomAxiosInstance> = axios.create()) {
    super(new AxiosHttpClient(axiosInstance));
  }
}

const api: AxiosRestApplicationClient = new AxiosRestApplicationClient();

for (const method of [ "request", "delete", "get", "head", "options", "post", "put", "patch" ]) {
  api.httpClient.axios["$" + method] = function() {
    return this[method].apply(this, arguments).then(res => res && res.data);
  };
}

export default boot(({ app, router, urlPath, redirect }) => {
  const { storageToken, storageCurrentRoute } = useAppLocalStorage();

  app.config.globalProperties.$axios = axios;
  app.config.globalProperties.$api = api;

  api.httpClient.axios.interceptors.request.use((config) => {
    if (storageToken.value != null) {
      config.headers.common["Authorization"] = `Bearer ${ storageToken.value }`;
    }

    return config;
  });

  api.httpClient.axios.interceptors.response.use((response) => {
    const data = response.data;

    if (data?.type === "message") {
      Notify.create({
        message: data.message,
        color: data.detail?.color ?? "negative",
        textColor: data.textColor ?? "white",
      });
    }

    return response;
  }, async function(error: AxiosError) {
    const response: AxiosResponse = error.response;

    let authStore = useAuthStore();

    switch (response.status) {
      case 400:
        if (response.data?.tag === "jwt") {
          Notify.create({
            message: "Erreur lors de la récupération du token: " + error.response.data.message,
            color: "negative",
          });
          await authStore.disconnect();
          delete error.config.headers["Authorization"];
          const result = await api.httpClient.axios.request(error.config);
          return Promise.resolve(result);
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
      case 500:
        Dialog.create({
          component: ErrorDialog,
          componentProps: {
            error: error,
          },
        });
        break;
      default:
        break;
    }

    return Promise.reject(error);
  });
});

export { api };
