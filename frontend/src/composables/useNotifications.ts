import createUuid from "@/utils/create-uuid";

const notifications = ref<AppNotification[]>([]);

const defaultNotificationOptions: AppNotificationCreateOptions = {
  type: "info",
  message: "Ooops! A message was not provided.",
  autoClose: true,
  duration: 5,
};

export default function useNotifications() {
  const createNotification = (options: AppNotificationCreateOptions) => {
    const _options = Object.assign({ ...defaultNotificationOptions }, options);
    notifications.value.push(<AppNotification> {
      id: createUuid(),
      ..._options,
    });
  };

  const removeNotifications = (id: string) => {
    const index = notifications.value.findIndex((item) => item.id === id);
    if (index !== -1) notifications.value.splice(index, 1);
  };

  const stopBodyOverflow = () => {
    document && document.body.classList.add(...[ "hide-overflow" ]);
  };

  const allowBodyOverflow = () => {
    document && document.body.classList.remove(...[ "hide-overflow" ]);
  };

  return {
    notifications,
    createNotification,
    removeNotifications,
    stopBodyOverflow,
    allowBodyOverflow,
  };
}
