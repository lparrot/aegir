import { defineStore } from "pinia";
import useAegir from "src/composables/useAegir";
import { useAuthStore } from "stores/auth";

interface StateInformations {
  userProjects: [];
  // projectItems: [];
  selectedProject: any;
  selectedItem: any;
}

const authStore = useAuthStore();
const { storageSidebar, projectRepository, projectItemsRepository } = useAegir();

export const useProjectStore = defineStore("project", {
  state: (): StateInformations => ({
    userProjects: [],
    // projectItems: null,
    selectedProject: null,
    selectedItem: null,
  }),

  getters: {},

  actions: {
    // async fetchItems() {
    //   if (authStore.isLoggedIn && this.selectedProject != null) {
    //     const { success, result } = await projectRepository.getItemsById(this.selectedProject);
    //     if (success) {
    //       this.projectItems = result;
    //     }
    //   }
    // },

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
