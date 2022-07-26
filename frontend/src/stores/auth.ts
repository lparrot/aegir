import { api } from "boot/axios";
import { LocalStorage, Notify } from "quasar";
import { defineStore } from "pinia";
import { LoginParams } from "src/models/auth";
import { getInitials } from "src/utils/string.utils";

export const useAuthStore = defineStore("auth", {
  state: () => ({
    user: null,
  }),

  getters: {
    isLoggedIn(state) {
      return state.user != null;
    },
    getInitials(state) {
      if (state.user != null) {
        return getInitials(state.user.username);
      }
      return null;
    },
  },

  actions: {
    async login(formData: LoginParams) {
      const { server_error, access_token } = await api.$post("/api/auth/login", formData);

      if (!server_error && access_token != null) {
        LocalStorage.set("token", access_token);
        await this.refreshUser();

        Notify.create({
          message: `Vous êtes connecté sous ${this.user.username}`,
          color: "positive",
        });

        this.$router.push("/dashboard");
      }
    },

    async disconnect() {
      await this.$router.push("/login");
      LocalStorage.remove("token");
      this.user = null;
    },

    async refreshUser() {
      const data = await api.get("/api/auth/user");

      if (data != null) {
        this.user = data;
      }
    },
  },
});
