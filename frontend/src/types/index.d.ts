import useSecurity from "@use/useSecurity";
import { AxiosInstance, AxiosRequestConfig } from "axios";
import "pinia";
import type { Ref } from "vue";
import "vue-router";
import { Router } from "vue-router";

declare module "@vue/runtime-core" {
  interface ComponentCustomProperties {
    $axios: AxiosInstance;
    $security: useSecurity;
    $log: (data: any) => any;
  }
}

declare module "pinia" {
  export interface PiniaCustomProperties {
    router: Router;
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

declare module "vue-router" {
  interface RouteMeta {
    access?: Array<string> | boolean;
    landing_page?: boolean;
    no_match?: boolean;
    title?: string;
    hideTitle?: boolean;
    with_workspaces?: boolean;
  }
}

interface FloatProps {
  as?: string | Component;
  show?: boolean;
  placement?: Placement;
  strategy?: Strategy;
  offset?: OffsetOptions;
  shift?: boolean | number | Partial<ShiftOptions & DetectOverflowOptions>;
  flip?: boolean | number | Partial<FlipOptions & DetectOverflowOptions>;
  arrow?: boolean | number;
  autoPlacement?: boolean | Partial<AutoPlacementOptions & DetectOverflowOptions>;
  autoUpdate?: boolean | Partial<AutoUpdateOptions>;
  zIndex?: number | string;
  enter?: string;
  enterFrom?: string;
  enterTo?: string;
  leave?: string;
  leaveFrom?: string;
  leaveTo?: string;
  originClass?: string | OriginClassResolver;
  tailwindcssOriginClass?: boolean;
  portal?: boolean | string;
  transform?: boolean;
  middleware?: Middleware[] | ((refs: {
    referenceEl: Ref<HTMLElement | null>,
    floatingEl: Ref<HTMLElement | null>,
  }) => Middleware[]);
}

type OriginClassResolver = (placement: Placement) => string
