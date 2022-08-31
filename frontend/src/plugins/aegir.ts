import useAegir from "@/composables/useAegir";
import { appMenu } from "@/router/routes";
import useMenu from "@use/useMenu";
import { useEventBus, UseEventBusReturn } from "@vueuse/core";
import { App, DirectiveBinding, ref } from "vue";

export default {
  install: (app: App, _options) => {
    const { init } = useAegir();
    const { setMenuDefault } = useMenu();

    init(app);

    setMenuDefault(appMenu);

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
