import { RouteRecord } from "vue-router";

export interface MenuItem {
  label?: string;
  type?: "header" | "separator";
  defaultOpened?: boolean;
  children?: MenuItem[];
  icon?: string;
  to?: string | Partial<RouteRecord>;
  showIf?: Function | boolean;
}
