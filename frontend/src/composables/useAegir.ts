import useAppLocalStorage from "src/composables/useAppLocalStorage";
import useAppEventBus from "src/composables/useAppEventBus";
import useMenu from "src/composables/useMenu";
import useAppRepository from "src/composables/repositories/useAppRepository";
import useAuthRepository from "src/composables/repositories/useAuthRepository";
import useProjectItemsRepository from "src/composables/repositories/useProjectItemsRepository";
import useProjectRepository from "src/composables/repositories/useProjectRepository";

export default function useAegir() {
  return {
    ...useAppLocalStorage(),
    ...useMenu(),
    bus: { ...useAppEventBus() },

    /* Repositories */
    appRepository: useAppRepository(),
    authRepository: useAuthRepository(),
    projectItemsRepository: useProjectItemsRepository(),
    projectRepository: useProjectRepository(),
  };
}
