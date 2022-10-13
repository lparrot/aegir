import Modal from "@/components/shared/overlay/Modal.vue";
import { app } from "@use/useAegir";
import { h, render, Suspense } from "vue";

export default class DialogService {
  create(options: DialogCreateOptions) {
    if (options == null) {
      return;
    }

    const { component, props, ...other } = options;

    let container = document.createElement("div");
    document.body.appendChild(container);

    let instance: any;

    if (options.component != null) {
      instance = h(component, {
        modelValue: true,
        ...other,
        ...props,
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

    render(h(Suspense, instance), container);

    return {
      hide: instance.component?.proxy?.hide,
      container,
    };
  }
}

