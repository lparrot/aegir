import { RouteLocationRaw } from "vue-router";

declare global {
  interface MenuItem {
    label: string;
    route?: RouteLocationRaw;
    showIf?: (() => boolean) | boolean;
    children?: Array<MenuItem>;
  }

  interface DatatableField {
    key: string;
    label?: string;
    fieldName?: string;
    transform?: (value: any) => any;
  }
}
