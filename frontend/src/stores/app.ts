import { api } from "@/api";
import { defineStore } from "pinia";

interface StateInformations {
  informations: { [index: string]: any };
}

export const useAppStore = defineStore("app", {
  state: (): StateInformations => ({
    informations: null,
  }),

  getters: {},

  actions: {
    async getInformations() {
      const { success, result } = await api.getAppInformations();

      if (success) {
        this.informations = result;
      }
    },
  },
});
