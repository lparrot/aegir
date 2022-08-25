import { defineStore } from "pinia";
import { api } from "boot/axios";

interface StateInformations {
  informations?: any;
}

export const useAppStore = defineStore("app", {
  state: (): StateInformations => ({
    informations: {},
  }),

  getters: {},

  actions: {
    async fetchInformations() {
      const data = await api.getAppInformations();

      if (data?.success) {
        this.informations = data.result;
      }
    },
  },
});
