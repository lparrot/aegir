import DialogService from "@/services/dialog.service";
import ToastService from "@/services/toast.service";
import { App, Ref } from "vue";

let dialog: DialogService;
let toast: ToastService;
let notifications: Ref<AppNotification[]> = ref<AppNotification[]>([]);

export let app: App = null;

export default function useAegir() {

  const init = (_app: App) => {
    app = _app;
    dialog = new DialogService();
    toast = new ToastService();
  };

  return {
    dialog,
    init,
    notifications,
    toast,
  };
}
