const defaultNotificationOptions: AppNotificationCreateOptions = {
  type: "info",
  message: "Ooops! A message was not provided.",
  autoClose: true,
  duration: 5000,
};

export default class ToastService {
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
