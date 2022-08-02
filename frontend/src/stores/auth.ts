import { Notify } from "quasar";
import { defineStore } from "pinia";
import { LoginParams } from "src/models/auth.model";
import { getInitials } from "src/utils/string.utils";
import useAppEventBus from "src/composables/useAppEventBus";
import useAppLocalStorage from "src/composables/useAppLocalStorage";
import useAuthRepository from "src/composables/repositories/useAuthRepository";

interface StateInformations {
  user?: any;
}

const bus = useAppEventBus();
const { storageToken, resetLocalStorage } = useAppLocalStorage();
const authRepository = useAuthRepository();

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

      const { success, result } = await authRepository.login(formData.username, formData.password);

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
      const { success, result } = await authRepository.getUser();

      if (success) {
        this.user = result;
      }
    },
  },
});
