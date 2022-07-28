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
      const { success, data } = await api.$get("/api/app/informations");
      if (success) {
        this.informations = data;
      }
    },
  },
});
