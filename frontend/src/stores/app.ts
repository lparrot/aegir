import { api } from "@/api";
import { appMenu } from "@/router/routes";
import { useAuthStore } from "@/stores/auth";
import useMenu from "@use/useMenu";
import useWebsocket from "@use/useWebsocket";
import { defineStore } from "pinia";

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
      const socket = useWebsocket();

      if (socket.client.value == null) {
        socket.initialize();
        await socket.connect();
      }
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
