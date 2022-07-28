import { api } from "boot/axios";
import { Notify } from "quasar";
import { defineStore } from "pinia";
import { LoginParams } from "src/models/auth.model";
import { getInitials } from "src/utils/string.utils";
import useAppEventBus from "src/composables/useAppEventBus";
import useAppLocalStorage from "src/composables/useAppLocalStorage";

interface StateInformations {
  user?: any;
}

export const useAuthStore = defineStore("auth", {
  state: (): StateInformations => ({
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
      const bus = useAppEventBus();
      const { storageToken } = useAppLocalStorage();

      const { success, data } = await api.$post("/api/auth/login", formData);

      if (success) {
        storageToken.value = data.token;
        this.user = data.claims;

        bus.$emit("connected");

        await this.$router.push({ name: "dashboard" });

        Notify.create({
          message: `Vous êtes connecté sous ${this.user.username}`,
          color: "positive",
        });
      }
    },

    async disconnect() {
      const { resetLocalStorage } = useAppLocalStorage();
      resetLocalStorage();
      this.user = null;
      await this.$router.push({ name: "login" });
    },

    async refreshUser() {
      const { success, data } = await api.$get("/api/auth/user");

      if (success) {
        this.user = data;
      }
    },
  },
});
