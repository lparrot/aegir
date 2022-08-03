import { api } from "boot/axios";
import { ApiResponse } from "src/models/api.model";

export default function useAuthRepository() {
  return {
    async login(username, password): Promise<ApiResponse<any>> {
      return api.$post("/api/auth/login", { username, password });
    },

    async getUser(): Promise<ApiResponse<any>> {
      return api.$get("/api/auth/user");
    },

    async getUserData(): Promise<ApiResponse<any>> {
      return api.$get("/api/auth/user_data");
    },
  };
}
