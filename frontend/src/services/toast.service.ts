import Toast from "src/components/shared/overlay/Toast.vue";
import ToastGroup from "src/components/shared/overlay/ToastGroup.vue";

import { createVNode, render, VNode } from "vue";

const defaultNotificationOptions: AppNotificationCreateOptions = {
  type: "info",
  message: "Ooops! A message was not provided.",
  autoClose: true,
  duration: 5,
};

export default class ToastService {
  toastGroup: VNode = null;

  constructor() {

    const instance = createVNode(
      ToastGroup,
      {
        placement: "bottom",
      },
      null,
    );

    Object.assign({}, instance.appContext, app._context);

    render(instance, document.body);

    this.toastGroup = instance;
  }

  public createToast(options: AppNotificationCreateOptions) {
    const { notifications } = useAegir();
    const _options = Object.assign({ ...defaultNotificationOptions }, options);
    const notification = <AppNotification> {
      id: createUuid(),
      ..._options,
    };

    notifications.value.push(notification);

    const instance = createVNode(
      Toast,
      {
        notification,
        onClose: () => {
          notifications.value = notifications.value.filter(n => n.id !== notification.id);
        },
      },
      null,
    );

    Object.assign({}, instance.appContext, app._context);

    this.toastGroup.el.appendChild(instance);
  }
}
