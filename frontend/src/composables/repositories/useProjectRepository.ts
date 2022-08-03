import { api } from "boot/axios";
import { ApiResponse } from "src/models/api.model";

export default function useProjectRepository() {
  return {
    async getUserProjects(): Promise<ApiResponse<any>> {
      return api.$get("/api/projects/current_user");
    },

    async getById(id): Promise<ApiResponse<any>> {
      return api.$get(`/api/projects/${id}`);
    },
  };
}
