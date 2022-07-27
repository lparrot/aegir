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
    async getInformations() {
      this.informations = await api.$get("/api/app/informations");
    },
  },
});
