import { RouteRecordRaw } from "vue-router";

/**
 * Ajouter meta.access: boolean|Array pour définir la sécurité pour la page (true, false, [] pour dire que l'utilisateur doit être connecté ou ['ROLE1','ROLE2'] pour les rôles que l'utilisateur doit posséder au minimum
 */
const routes: RouteRecordRaw[] = [
  {
    path: "/", component: () => import("layouts/default.vue"), children: [
      { name: "index", path: "", component: () => import("pages/index.vue"), meta: { access: true } },
      { name: "login", path: "login", component: () => import("pages/login.vue"), meta: { access: true } },
      { name: "profile", path: "profile", component: () => import("pages/profile.vue"), meta: { access: [] } },
      { name: "dashboard", path: "dashboard", component: () => import("pages/dashboard.vue"), meta: { access: [ "USER" ] } },
      { name: "tasks", path: "tasks", component: () => import("pages/tasks.vue"), meta: { access: [ "USER" ] } },
      {
        name: "admin", path: "admin", component: () => import("components/BlankPage.vue"), children: [
          { name: "admin-connections", path: "connections", component: () => import("pages/admin/connections.vue"), meta: { access: [ "ADMIN" ] } },
        ],
      },
    ],
  },
  {
    path: "/errors", component: () => import("components/BlankPage.vue"), children: [
      { name: "errors-401", path: "access-denied", component: () => import("pages/errors/access-denied.vue") },
      { name: "errors-502", path: "bad-gateway", component: () => import("pages/errors/bad-gateway.vue") },
    ],
  },
  { name: "errors-404", path: "/:catchAll(.*)*", component: () => import("pages/errors/not-found.vue"), meta: { no_match: true } },
];

export default routes;
