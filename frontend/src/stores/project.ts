import { defineStore } from "pinia";
import { api } from "boot/axios";
import { useAuthStore } from "stores/auth";
import useAppLocalStorage from "src/composables/useAppLocalStorage";
import useProjectRepository from "src/composables/repositories/useProjectRepository";

interface StateInformations {
  projectItems: [];
  selectedItem: any;
}

const authStore = useAuthStore();
const projectRepository = useProjectRepository();
const { storageSidebar } = useAppLocalStorage();

export const useProjectStore = defineStore("project", {
  state: (): StateInformations => ({
    projectItems: null,
    selectedItem: null,
  }),

  getters: {},

  actions: {
    async fetchItems() {

      if (authStore.isLoggedIn) {
        const { success, result } = await projectRepository.getById(1);
        if (success) {
          this.projectItems = result;
        }
      }
    },

    async fetchSelectedItem() {

      if (authStore.isLoggedIn && storageSidebar.value.item_selected != null) {
        const { success, result } = await api.$get(`/api/project_items/${storageSidebar.value.item_selected}`);
        if (success) {
          this.selectedItem = result;
        }
      }
    },
  },
});
