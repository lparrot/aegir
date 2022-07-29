import { api } from "boot/axios";

export default function useProjectItemsRepository() {
  return {
    async getById(id) {
      return api.$get(`/api/project_items/${id}`);
    },
  };
}
