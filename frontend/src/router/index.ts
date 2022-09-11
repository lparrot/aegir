import routes from "@/router/routes";
import { useAppStore } from "@/stores/app";
import { useAuthStore } from "@/stores/auth";
import useSecurity from "@use/useSecurity";
import { createRouter, createWebHistory, RouteLocationNormalized } from "vue-router";

const PAGE_ACCESS_DENIED = { name: "errors-401" };

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  scrollBehavior: () => ({ left: 0, top: 0 }),
  routes,
});

router.beforeResolve(async (to, from, next) => {
  const appStore = useAppStore();
  const authStore = useAuthStore();

  if (!appStore.started) {
    // Si connexion
    await appStore.onStartup();
  }

  if (to.meta.no_match) {
    // Si erreur 404
    return next();
  }

  if (authStore.user != null) {
    // Si utilisateur connecté
    if (to.meta.landing_page) {
      // Si page layout landing, redirection vers page de tâches
      return next({ name: "tasks" });
    }

    if (checkAccess(to)) {
      // Si autorisé à afficher la page
      return next();
    }
  } else {
    // Si utilisateur déconnecté
    if (!to.meta.landing_page) {
      return next({ name: "login" });
    }
  }

  // Sinon accès refusé
  next(PAGE_ACCESS_DENIED);
});

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
    if (authStore.user == null) {
      // Si l'utilisateur n'est pas connecté, accès refusé
      return false;
    }

    return isInRoles(...access);
  }

  return true;
};
