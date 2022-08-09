import { defineStore } from "pinia";
import { useAuthStore } from "stores/auth";
import useAppLocalStorage from "src/composables/useAppLocalStorage";
import { ProjectInfo } from "app/.generated/rest";
import { api } from "boot/axios";

interface StateInformations {
  userProjects: ProjectInfo[];
  selectedProject: ProjectInfo;
  selectedItem: any;
}

const authStore = useAuthStore();
const { storageSidebar } = useAppLocalStorage();

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
        const { success, result } = await api.getProjectsByCurrentUser();
        if (success) {
          this.userProjects = result;
        }
      }
    },

    async fetchSelectedProject() {
      if (authStore.isLoggedIn && storageSidebar.value.project_selected != null) {
        const { success, result } = await api.getProjectsById(storageSidebar.value.project_selected);
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
        const { success, result } = await api.getProjectItemsById(storageSidebar.value.item_selected);
        if (success) {
          this.selectedItem = result;
        }
      }
    },
  },
});
