import { api } from "boot/axios";

export default function useAppRepository() {
  return {
    async getInformations() {
      return api.$get("/api/app/informations");
    },
  };
}
