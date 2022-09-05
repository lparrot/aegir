import { useAuthStore } from "@/stores/auth";

interface StateInformations {
  started: boolean;
  informations: { [index: string]: any };
}

export const useAppStore = defineStore("app", {
  state: (): StateInformations => ({
    started: false,
    informations: null,
  }),

  getters: {},

  actions: {
    async onStartup() {
      const authStore = useAuthStore();
      await authStore.getUser();
      const { setMenuDefault } = useMenu();
      setMenuDefault(appMenu);
      this.started = true;
    },

    async getInformations() {
      const { success, result } = await api.getAppInformations();

      if (success) {
        this.informations = result;
      }
    },
  },
});
