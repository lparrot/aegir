import { route } from "quasar/wrappers";
import { createMemoryHistory, createRouter, createWebHashHistory, createWebHistory, RouteLocationNormalized, Router } from "vue-router";

import routes from "./routes";
import { useAuthStore } from "stores/auth";
import useAppLocalStorage from "src/composables/useAppLocalStorage";

const PAGE_ACCESS_DENIED = { name: "errors-401" };

export let router: Router = null;

export default route(function(/* { store, ssrContext } */) {
  const { storageCurrentRoute } = useAppLocalStorage();

  const createHistory = process.env.SERVER
    ? createMemoryHistory
    : (process.env.VUE_ROUTER_MODE === "history" ? createWebHistory : createWebHashHistory);

  router = createRouter({
    scrollBehavior: () => ({ left: 0, top: 0 }),
    routes,
    history: createHistory(process.env.VUE_ROUTER_BASE),
  });

  router.beforeEach((to, from, next) => {
    if (checkAccess(to)) {
      if (to.name != undefined && to.name !== "errors-502") {
        storageCurrentRoute.value = to.path;
      }
      return next();
    }
    next(PAGE_ACCESS_DENIED);
  });

  return router;
});

export const checkAccess = (route: RouteLocationNormalized) => {
  if (route == null || route.meta.no_match) {
    // Si la route est vide ou si elle correspond à une 404
    return false;
  }

  const access: Array<string> | boolean | null = route.meta.access as any;

  const authStore = useAuthStore();

  // Si aucune sécurité
  if (access == null || access === true) {
    return true;
  }

  // Si aucun accès autorisé
  if (access === false) {
    return false;
  }

  // Si 'access' est un array
  if (Array.isArray(access)) {
    if (!authStore.isLoggedIn) {
      // Si l'utilisateur n'est pas connecté, accès refusé
      return false;
    }

    if (!access.every(role => authStore.user.roles.indexOf(role) > -1)) {
      // Si l'utilisateur ne possède pas au moins tous les rôles, accès refusé
      return false;
    }
  }

  return true;
};
