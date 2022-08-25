import useAegir from "@/composables/useAegir";
import { App } from "vue";

export default {
  install: (app: App, _options) => {
    const { init } = useAegir();

    init(app);

    app.config.errorHandler = (err: any, instance, info) => {
      console.groupCollapsed(`%c[ERR:] ${ err.message }`, "background: #FF0748; color: #FFF");
      console.log(`%cinfo: ${ info }`, "color: #FF0748");
      console.error(err);
      console.groupEnd();
    };
  },
};
