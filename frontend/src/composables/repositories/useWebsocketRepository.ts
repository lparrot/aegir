import { api } from "boot/axios";
import { ApiResponse } from "src/models/api.model";

export default function useWebsocketRepository() {
  return {
    getConnections(): Promise<ApiResponse<any>> {
      return api.$get("/api/websockets");
    },
  };
}
