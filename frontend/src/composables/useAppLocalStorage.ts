import { useLocalStorage } from "@vueuse/core";

export const STORAGE_TOKEN = "aegir.token";
export const STORAGE_SIDEBAR = "aegir.sidebar";
export const STORAGE_CURRENT_ROUTE = "aegir.current_route";

const storageToken = useLocalStorage(STORAGE_TOKEN, null);
const storageSidebar = useLocalStorage(STORAGE_SIDEBAR, { module_selected: null, item_selected: null, project_selected: null, items_expanded: [] });
const storageCurrentRoute = useLocalStorage(STORAGE_CURRENT_ROUTE, null);

export default function useAppLocalStorage() {

  const resetLocalStorage = () => {
    storageToken.value = null;
    storageSidebar.value = { module_selected: null, item_selected: null, project_selected: null, items_expanded: [] };
    storageCurrentRoute.value = null;
  };

  return { resetLocalStorage, storageToken, storageSidebar, storageCurrentRoute };
};
