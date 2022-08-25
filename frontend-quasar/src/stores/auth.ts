import { Notify } from "quasar";
import { defineStore } from "pinia";
import { LoginParams } from "src/models/auth.model";
import { getInitials } from "src/utils/string.utils";
import useAppEventBus from "src/composables/useAppEventBus";
import useAppLocalStorage from "src/composables/useAppLocalStorage";
import { api } from "boot/axios";

interface StateInformations {
  user?: any;
}

const bus = useAppEventBus();
const { storageToken, resetLocalStorage } = useAppLocalStorage();

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

      const { success, result } = await api.postAuthlogin({ username: formData.username, password: formData.password });

      if (success) {
        storageToken.value = result.token;
        this.user = result.claims;

        bus.$emit("connected");

        await this.$router.push({ name: "tasks" });

        Notify.create({
          message: `Vous êtes connecté sous ${ this.user.username }`,
          color: "positive",
        });
      }
    },

    async disconnect() {
      resetLocalStorage();
      this.user = null;
      bus.$emit("disconnected");
      await this.$router.push({ name: "login" });
    },

    async refreshUser() {
      const { success, result } = await api.getAuthUser();

      if (success) {
        this.user = result;
      }
    },
  },
});
