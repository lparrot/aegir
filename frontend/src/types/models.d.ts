import { RouteLocationRaw, RouteRecordNormalized } from "vue-router";

declare global {
  interface AppEventBus<T = any> {
    type: string,
    data?: T
  }

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

  interface DialogCreateOptions {
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

  type AppNotificationType = "success" | "info" | "warn" | "danger"

  interface AppNotification {
    id: string;
    type: AppNotificationType;
    title: string;
    message: string;
    autoClose: boolean;
    duration: number;
  }

  interface AppNotificationCreateOptions {
    type?: AppNotificationType;
    title?: string;
    message?: string;
    autoClose?: boolean;
    duration?: number;
  }

  interface AppLoaderOptions {
    icon?: any;
    iconClass?: string;
    message?: string;
    messageClass?: string;
  }

  interface AppCommanderItem {
    type?: "route";
    icon?: any;
    label?: string;
    action?: () => void;
    route?: RouteRecordNormalized;
  }
}
