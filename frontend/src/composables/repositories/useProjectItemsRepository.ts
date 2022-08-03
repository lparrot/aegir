import { api } from "boot/axios";
import { ApiResponse } from "src/models/api.model";

export default function useProjectItemsRepository() {
  return {
    async getById(id): Promise<ApiResponse<any>> {
      return api.$get(`/api/project_items/${id}`);
    },
  };
}
