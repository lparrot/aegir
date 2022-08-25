import { boot } from "quasar/wrappers";
import { useAuthStore } from "stores/auth";
import useAppEventBus from "src/composables/useAppEventBus";
import useAppLocalStorage from "src/composables/useAppLocalStorage";

export default boot(async ({}) => {
  ////////////////
  // Composables
  ////////////////
  const authStore = useAuthStore();
  const bus = useAppEventBus();
  const { storageToken } = useAppLocalStorage();

  if (storageToken.value != null) {
    await authStore.refreshUser();
    bus.$emit("connected");
  }
});
