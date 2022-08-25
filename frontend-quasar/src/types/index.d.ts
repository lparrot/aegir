import "pinia";
import { Router } from "vue-router";
import { AxiosInstance, AxiosRequestConfig } from "axios";

declare module "pinia" {
  export interface PiniaCustomProperties {
    $router: Router;
  }
}

interface CustomAxiosInstance extends AxiosInstance {
  $request<T = any, R = any>(config: AxiosRequestConfig): Promise<R>;

  $get<T = any, R = any>(url: string, config?: AxiosRequestConfig): Promise<R>;

  $delete<T = any, R = any>(url: string, config?: AxiosRequestConfig): Promise<R>;

  $head<T = any, R = any>(url: string, config?: AxiosRequestConfig): Promise<R>;

  $options<T = any, R = any>(url: string, config?: AxiosRequestConfig): Promise<R>;

  $post<T = any, R = any>(url: string, data?: any, config?: AxiosRequestConfig): Promise<R>;

  $put<T = any, R = any>(url: string, data?: any, config?: AxiosRequestConfig): Promise<R>;

  $patch<T = any, R = any>(url: string, data?: any, config?: AxiosRequestConfig): Promise<R>;
}
