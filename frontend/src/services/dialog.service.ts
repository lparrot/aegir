import Modal from "@/components/shared/overlay/Modal.vue";
import { render, VNode } from "vue";

export default class DialogService {
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

