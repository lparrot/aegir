import { createRouter, createWebHistory, RouteLocationNormalized } from "vue-router";
import { useAuthStore } from "@/stores/auth";
import routes from "@/router/routes";

const PAGE_ACCESS_DENIED = { name: "errors-401" };

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  scrollBehavior: () => ({ left: 0, top: 0 }),
  routes,
});

router.beforeEach((to, from, next) => {
  if (to.meta.no_match) {
    return next();
  }

  if (checkAccess(to)) {
    return next();
  }
  next(PAGE_ACCESS_DENIED);
});

export default router;

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
