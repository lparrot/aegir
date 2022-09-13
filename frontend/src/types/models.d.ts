import { RouteLocationRaw, RouteRecordNormalized } from "vue-router";

declare global {
  type TailwindColor = "slate" | "gray" | "zinc" | "neutral" | "stone" | "red" | "orange" | "amber" | "yellow" | "lime" | "green" | "emerald" | "teal" | "cyan" | "sky" | "blue" | "indigo" | "violet" | "purple" | "fuchsia" | "pink" | "rose"
  type TailwindVariant = "primary" | "secondary" | "success" | "info" | "warn" | "danger"
  type TailwindColorAndVariant = TailwindColor | TailwindVariant

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
    field?: string;
    transform?: (value: any, item: any) => any;
    preventClick?: boolean;
  }

  interface DialogCreateOptions {
    backdropDismiss?: boolean;
    component?: any,
    description?: string;
    labelCancel?: string;
    labelOk?: string;
    message?: string,
    modelValue?: boolean;
    onCancel?: (payload?) => any,
    onClose?: () => any
    onHide?: () => any
    onOk?: (payload?) => any,
    panelClasses?: string | string[];
    props?: { [index: string]: any },
    showCancel?: boolean
    showButtons?: boolean
    title?: string;
  }

  interface AppTreeItem {
    children?: AppTreeItem[];
    icon?: any;
    label: string;
    opened?: boolean;
    selectable?: boolean;
    type?: string;
    value: any;
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
