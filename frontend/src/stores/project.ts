import { defineStore } from "pinia";
import { api } from "boot/axios";
import { useAuthStore } from "stores/auth";
import useAppLocalStorage from "src/composables/useAppLocalStorage";

interface StateInformations {
  projectItems: [];
  selectedItem: any;
}

export const useProjectStore = defineStore("project", {
  state: (): StateInformations => ({
    projectItems: null,
    selectedItem: null,
  }),

  getters: {},

  actions: {
    async fetchItems() {
      const authStore = useAuthStore();

      if (authStore.isLoggedIn) {
        const { success, data } = await api.$get(`/api/projects/${1}/items`);
        if (success) {
          this.projectItems = data;
        }
      }
    },

    async fetchSelectedItem() {
      const authStore = useAuthStore();
      const { storageSidebar } = useAppLocalStorage();

      if (authStore.isLoggedIn && storageSidebar.value.item_selected != null) {
        const { success, data } = await api.$get(`/api/project_items/${storageSidebar.value.item_selected}`);
        if (success) {
          this.selectedItem = data;
        }
      }
    },
  },
});
