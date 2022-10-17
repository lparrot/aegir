import { RouteLocationRaw, RouteRecordNormalized } from "vue-router";

declare global {
  type TailwindColor = "slate" | "gray" | "zinc" | "neutral" | "stone" | "red" | "orange" | "amber" | "yellow" | "lime" | "green" | "emerald" | "teal" | "cyan" | "sky" | "blue" | "indigo" | "violet" | "purple" | "fuchsia" | "pink" | "rose"
  type TailwindVariant = "primary" | "secondary" | "success" | "info" | "warn" | "danger"
  type TailwindColorAndVariant = TailwindColor | TailwindVariant
  type DatatableFieldAlign = "left" | "center" | "right"

  interface AppEventBus<T = any> {
    type: string,
    data?: T
  }

  interface AppTaskColumn {
    id: string;
    header?: string;
    headerClass?: string;
    rowClass?: string;
    componentWidth?: string;
    component: any;
  }

  interface MenuItem {
    label: string;
    route?: RouteLocationRaw;
    showIf?: (() => boolean) | boolean;
    children?: Array<MenuItem>;
  }

  interface DatatableField {
    align?: DatatableFieldAlign;
    compact?: boolean;
    field?: string;
    key: string;
    label?: string;
    preventClick?: boolean;
    sortable?: boolean;
    tdClass?: string | string[] | any;
    thClass?: string | string[] | any;
    transform?: (value: any, item: any) => any;
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

  interface AppPagination {
    count?: number;
    order?: AppPaginationOrder;
    page?: number;
    size?: number;
    totalPage?: number;
  }

  interface AppPaginationOrder {
    asc?: boolean;
    field?: string;
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
  type AppFetchMethodType = "get" | "put" | "post" | "delete"

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
