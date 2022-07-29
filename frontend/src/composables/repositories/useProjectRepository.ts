import { api } from "boot/axios";

export default function useProjectRepository() {
  return {
    async getUserProjects() {
      return api.$get("/api/projects/current_user");
    },

    async getById(id) {
      return api.$get(`/api/projects/${id}`);
    },
  };
}
