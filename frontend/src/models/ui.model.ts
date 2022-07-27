import { RouteRecord } from "vue-router";

export class MenuItem {
  label?: string;
  type?: "header" | "separator";
  defaultOpened?: boolean;
  children?: MenuItem[];
  icon?: string;
  to?: string | RouteRecord;
  showIf?: Function | boolean;
}
