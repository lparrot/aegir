import { api } from "@/api";
import { useAuthStore } from "@/stores/auth";
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

      const authStore = useAuthStore();

      await authStore.getUser();

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
