import { App, ComponentOptions, createApp, h, render, VNode } from "vue";
import Modal from "../components/shared/overlay/Modal.vue";

let app: App = null;

interface DialogCreateOptions {
  component?: ComponentOptions,
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

class DialogService {
  create(options: DialogCreateOptions) {
    if (options == null) {
      return;
    }

    let container = document.createElement("div");
    document.body.appendChild(container);

    let instance: VNode;

    if (options.component != null) {
      instance = h(options.component, {
        modelValue: true,
        onOk: options.onOk,
        onCancel: options.onCancel,
        onClose: options.onClose,
        onHide: options.onHide,
        ...options.props,
      });
    } else {
      instance = h(Modal, {
          modelValue: true,
          ...options,
        },
        {
          default: () => options.message,
        },
      );
    }

    if (app?._context) {
      instance.appContext = app._context;
    }

    render(instance, container);

    return { instance, container };
  }
}

let dialog: DialogService;

export default function useAegir() {

  const init = (application: App) => {
    app = application;
    dialog = new DialogService();
  };

  return {
    dialog,
    init,
  };
};

export function createChildApp(appCfg) {
  const childApp = createApp(appCfg);

  childApp.config.globalProperties = app.config.globalProperties;

  const { ...appContext } = app._context;
  Object.assign(childApp._context, appContext);

  return app;
}
