import { api } from "boot/axios";

export default function useAuthRepository() {
  return {
    async login(username, password) {
      return api.$post("/api/auth/login", { username, password });
    },

    async getUser() {
      return api.$get("/api/auth/user");
    },

    async getUserData() {
      return api.$get("/api/auth/user_data");
    },
  };
}
