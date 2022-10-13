import { MysqlType } from "back_types";
import sortBy from "lodash/sortBy";
import { defineStore } from "pinia";

interface StateInformations {
  mysql_types: MysqlType[];
}

export const useDataStore = defineStore("data", {
  state: (): StateInformations => ({
    mysql_types: null,
  }),

  getters: {},

  actions: {
    async fetchMysqlTypes() {
      if (this.mysql_types == null) {
        const { success, result } = await api.getMysqlTypes();
        if (success) {
          this.mysql_types = sortBy(result);
        }
      }
      return this.mysql_types;
    },
  },
});
