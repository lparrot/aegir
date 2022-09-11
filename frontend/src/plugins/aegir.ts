import { useAuthStore } from "@/stores/auth";
import useAegir from "@use/useAegir";
import { Bus } from "@use/useAppEventBus";
import useWebsocket from "@use/useWebsocket";
import { useEventBus, UseEventBusReturn } from "@vueuse/core";
import { EnumWebsocketMessageType, WebsocketTypedMessage } from "back_types";
import { App, DirectiveBinding, ref, watch } from "vue";

export default {
  install: async (app: App, _options) => {
    const { init } = useAegir();

    init(app);

    const authStore = useAuthStore();
    const socket = useWebsocket();
    const { toast } = useAegir();

    Bus.$on("login", async () => {
      await socket.initialize();

      await socket.subscribe("/topic/session", async (message: WebsocketTypedMessage) => {
        switch (message.type) {
          case EnumWebsocketMessageType.CONNECT:
          case EnumWebsocketMessageType.DISCONNECT:
            toast.createToast({
              message: `${ message.type }: ${ message.data?.user }`,
              type: "info",
            });
            break;
          default:
            break;
        }
      }, { id: "app-session" });

      await socket.subscribe("/user/topic/session", async (message: WebsocketTypedMessage) => {
        switch (message.type) {
          case EnumWebsocketMessageType.CLOSE_SESSION:
            await authStore.logout();
            toast.createToast({
              message: `Vous avez été déconnecté par l'action d'un administrateur`,
              type: "warn",
            });
            break;
          default:
            break;
        }
      }, { id: "user-session" });
    });

    Bus.$on("logout", () => {
      socket.unsubscribe("user-session");
    });

    watch(
      () => authStore.user,
      (_user) => {
        if (_user != null) {
          Bus.$emit("login");
        } else {
          Bus.$emit("logout");
        }
      },
      { immediate: true },
    );

    const bus_pool = ref<{ [index: string]: UseEventBusReturn<any, any> }>({});

    const overlayClickListener = (_binding: DirectiveBinding) => {
      const bus = bus_pool.value[_binding.arg];
      bus.emit();
    };

    app.directive("overlay", {
      mounted: (_el, _binding) => {
        const bus = bus_pool.value[_binding.arg] = useEventBus(_binding.arg);

        bus.on(() => {
          console.log("event");
          _binding.value();
        });
      },
      unmounted: (_el, _binding) => {
        bus_pool.value[_binding.arg].reset();
      },
    });

    const abortController = new AbortController();

    app.directive("close-overlay", {
      mounted: (_el, _binding) => {
        _el.addEventListener("click", () => overlayClickListener(_binding), { signal: abortController.signal });
      },
      unmounted: (_el, _binding) => {
        _el.addEventListener("click", () => abortController.abort());
      },
    });

    app.config.errorHandler = (err: any, instance, info) => {
      console.groupCollapsed(`%c[ERR:] ${ err.message }`, "background: #FF0748; color: #FFF");
      console.log(`%cinfo: ${ info }`, "color: #FF0748");
      console.error(err);
      console.groupEnd();
    };
  },
};
