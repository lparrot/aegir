import { defineStore } from "pinia";
import { api } from "boot/axios";

export interface StateInformations {
  version?: string;
}

export const useAppStore = defineStore("app", {
  state: () => ({
    informations: {} as StateInformations,
  }),

  getters: {},

  actions: {
    async getInformations() {
      this.informations = await api.get("/api/app/informations");
    },
  },
});
