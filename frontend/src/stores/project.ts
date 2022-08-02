import { defineStore } from "pinia";
import { useAuthStore } from "stores/auth";
import useAppLocalStorage from "src/composables/useAppLocalStorage";
import useProjectRepository from "src/composables/repositories/useProjectRepository";
import useProjectItemsRepository from "src/composables/repositories/useProjectItemsRepository";

interface StateInformations {
  userProjects: [];
  selectedProject: any;
  selectedItem: any;
}

const authStore = useAuthStore();
const { storageSidebar } = useAppLocalStorage();
const projectRepository = useProjectRepository();
const projectItemsRepository = useProjectItemsRepository();

export const useProjectStore = defineStore("project", {
  state: (): StateInformations => ({
    userProjects: [],
    selectedProject: null,
    selectedItem: null,
  }),

  getters: {},

  actions: {
    async fetchUserProjects() {
      if (authStore.isLoggedIn) {
        const { success, result } = await projectRepository.getUserProjects();
        if (success) {
          this.userProjects = result;
        }
      }
    },

    async fetchSelectedProject() {
      if (authStore.isLoggedIn && storageSidebar.value.project_selected != null) {
        const { success, result } = await projectRepository.getById(storageSidebar.value.project_selected);
        if (success) {
          this.selectedProject = result;
          if (storageSidebar.value.item_selected == null) {
            storageSidebar.value.item_selected = result.items[0]?.id;
          }
        }
      }
    },

    async fetchSelectedItem() {
      if (authStore.isLoggedIn && storageSidebar.value.item_selected != null) {
        const { success, result } = await projectItemsRepository.getById(storageSidebar.value.item_selected);
        if (success) {
          this.selectedItem = result;
        }
      }
    },
  },
});
