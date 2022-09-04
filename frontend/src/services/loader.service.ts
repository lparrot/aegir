import { ClockIcon } from "@heroicons/vue/24/outline";
import { Ref } from "vue";

const defaultLoaderOptions = {
  icon: ClockIcon,
  iconClass: "h-16 w-16 text-primary",
  messageClass: "text-2xl text-primary",
  message: "Chargement ...",
};

export default class LoaderService {

  show = ref(false);
  options: Ref<AppLoaderOptions> = ref<AppLoaderOptions>({});

  start(options?: AppLoaderOptions) {
    this.options.value = Object.assign({}, defaultLoaderOptions, options);
    this.show.value = true;
  }

  stop() {
    this.show.value = false;
  }
}
