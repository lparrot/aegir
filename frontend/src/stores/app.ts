import { useAuthStore } from "@/stores/auth";

interface StateInformations {
  informations: { [index: string]: any };
}

export const useAppStore = defineStore("app", {
  state: (): StateInformations => ({
    informations: null,
  }),

  getters: {},

  actions: {
    async onStartup() {
      console.log("ok");
      const authStore = useAuthStore();
      await authStore.getUser();
      const { setMenuDefault } = useMenu();
      setMenuDefault(appMenu);
    },

    async getInformations() {
      const { success, result } = await api.getAppInformations();

      if (success) {
        this.informations = result;
      }
    },
  },
});
