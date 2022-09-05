import useAegir from "@/composables/useAegir";
import useAppLocalStorage from "@/composables/useAppLocalStorage";

import type { CustomAxiosInstance } from "@/types";
import type { AxiosRequestConfig, AxiosResponse } from "axios";
import Axios, { AxiosError } from "axios";
import { HttpClient, RestApplicationClient, RestResponse } from "../../.generated/rest";

const { storageToken } = useAppLocalStorage();

class AxiosHttpClient implements HttpClient {

  constructor(public axios: Partial<CustomAxiosInstance>) {
  }

  request<R>(requestConfig: { method: string; url: string; queryParams?: any; data?: any; copyFn?: (data: R) => R; options?: AxiosRequestConfig; }): RestResponse<R> {
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

    const config: AxiosRequestConfig = {};
    config.method = requestConfig.method as typeof config.method;
    config.url = (requestConfig.url?.startsWith("/") ? "" : "/") + requestConfig.url;
    config.params = requestConfig.queryParams;
    config.data = requestConfig.data;
    config.maxContentLength = Infinity;
    config.maxBodyLength = Infinity;
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

  constructor(axiosInstance: Partial<CustomAxiosInstance> = Axios.create()) {
    super(new AxiosHttpClient(axiosInstance));
  }
}

export const api: AxiosRestApplicationClient = new AxiosRestApplicationClient();

for (const method of [ "request", "delete", "get", "head", "options", "post", "put", "patch" ]) {
  api.httpClient.axios["$" + method] = function() {
    return this[method].apply(this, arguments).then(res => res && res.data);
  };
}

api.httpClient.axios.interceptors.request.use((config) => {
  if (storageToken.value != null) {
    config.headers.common["Authorization"] = `Bearer ${storageToken.value}`;
  }

  return config;
});

api.httpClient.axios.interceptors.response.use((response) => {
  const { dialog } = useAegir();

  const data = response.data;

  if (data?.type === "message") {
    dialog.create({
      title: "Erreur",
      message: data.message,
    });
  }

  return response;
}, async function(error: AxiosError) {
  const response: AxiosResponse = error.response;
});

export const axios = api.httpClient?.axios;
