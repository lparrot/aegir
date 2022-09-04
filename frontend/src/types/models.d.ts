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

  export interface DialogCreateOptions {
    component?: any,
    message?: string,
    showCancel?: boolean
    onOk?: (payload?) => any,
    onCancel?: (payload?) => any,
    onClose?: () => any
    onHide?: () => any
    props?: { [index: string]: any },
    description?: string;
    labelCancel?: string;
    labelOk?: string;
    modelValue?: boolean;
    panelClasses?: string | string[];
    title?: string;
  }

  export type AppNotificationType = "success" | "info" | "warn" | "danger"

  export interface AppNotification {
    id: string;
    type: AppNotificationType;
    title: string;
    message: string;
    autoClose: boolean;
    duration: number;
  }

  export interface AppNotificationCreateOptions {
    type?: AppNotificationType;
    title?: string;
    message?: string;
    autoClose?: boolean;
    duration?: number;
  }
}
