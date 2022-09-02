import ToastGroup from "src/components/shared/overlay/ToastGroup.vue";

import { createVNode, render } from "vue";

const defaultNotificationOptions: AppNotificationCreateOptions = {
  type: "info",
  message: "Ooops! A message was not provided.",
  autoClose: true,
  duration: 5000,
};

export default class ToastService {
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
  }

  public createToast(options: AppNotificationCreateOptions) {
    const { notifications } = useAegir();

    const _options = Object.assign({ ...defaultNotificationOptions }, options);

    const notification = <AppNotification> {
      id: createUuid(),
      ..._options,
    };

    notifications.value.push(notification);
  }
}
