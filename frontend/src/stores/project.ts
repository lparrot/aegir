import { defineStore } from "pinia";
import { api } from "boot/axios";

interface StateInformations {
  selectedItemId?: number;
  selectedItem: any;
}

export const useProjectStore = defineStore("project", {
  state: (): StateInformations => ({
    selectedItemId: null,
    selectedItem: null,
  }),

  getters: {},

  actions: {
    async fetchSelectedItem() {
      if (this.selectedItemId != null) {
        this.selectedItem = await api.$get(`/api/project_items/${ this.selectedItemId }`);
      }
    },
  },
});
