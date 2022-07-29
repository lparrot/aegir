import { defineStore } from "pinia";
import useAppRepository from "src/composables/repositories/useAppRepository";

interface StateInformations {
  informations?: any;
}

const appRepository = useAppRepository();

export const useAppStore = defineStore("app", {
  state: (): StateInformations => ({
    informations: {},
  }),

  getters: {},

  actions: {
    async fetchInformations() {
      const data = await appRepository.getInformations();

      if (data?.success) {
        this.informations = data.result;
      }
    },
  },
});
