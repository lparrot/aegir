import { api } from "boot/axios";

export default function useProjectRepository() {
  return {
    async getById(id) {
      return api.$get(`/api/projects/${id}/items`);
    },
  };
}
