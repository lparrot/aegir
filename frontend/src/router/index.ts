import routes from "@/router/routes";
import { createRouter, createWebHistory, RouteLocationNormalized } from "vue-router";

const PAGE_ACCESS_DENIED = { name: "errors-401" };

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  scrollBehavior: () => ({ left: 0, top: 0 }),
  routes,
});

export const initRouter = () => {
  router.beforeEach((to, from, next) => {
    if (to.meta.no_match) {
      return next();
    }

    if (checkAccess(to)) {
      return next();
    }
    console.log("401");
    next(PAGE_ACCESS_DENIED);
  });
};

export default router;

export const checkAccess = (route: RouteLocationNormalized) => {
  if (route == null || route.meta.no_match) {
    // Si la route est vide ou si elle correspond à une 404
    return false;
  }

  const access = route.meta.access;

  const authStore = useAuthStore();
  const { isInRoles } = useSecurity();

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

    return isInRoles(...access);
  }

  return true;
};
