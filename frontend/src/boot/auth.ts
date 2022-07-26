import { LocalStorage } from "quasar";
import { boot } from "quasar/wrappers";
import { useAuthStore } from "stores/auth";

export default boot(async ({}) => {
  const authStore = useAuthStore();

  if (LocalStorage.has("aegir.token")) {
    await authStore.refreshUser();
  }
});
