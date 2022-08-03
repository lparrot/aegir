import { api } from "boot/axios";
import { ApiResponse } from "src/models/api.model";

export default function useAppRepository() {
  return {
    async getInformations(): Promise<ApiResponse<any>> {
      return api.$get("/api/app/informations");
    },
  };
}
