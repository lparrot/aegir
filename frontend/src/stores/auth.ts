interface StateInformations {
  user: { [index: string]: any };
}

const { storageToken } = useAppLocalStorage();

export const useAuthStore = defineStore("auth", {
  state: (): StateInformations => ({
    user: null,
  }),

  getters: {
    isLoggedIn(state) {
      return state.user != null;
    },
  },

  actions: {
    async login(params: { username: string, password: string }) {
      const { success, result } = await api.postAuthlogin(params);

      if (success) {
        this.user = result.claims;
        storageToken.value = result.token;
        return result;
      }

      return null;
    },

    async logout() {
      this.user = null;
      storageToken.value = null;
    },

    async getUser() {
      if (storageToken.value != null) {
        const { success, result } = await api.getAuthUser();

        if (success) {
          this.user = result;
        }

        return result;
      }
    },
  },
});
