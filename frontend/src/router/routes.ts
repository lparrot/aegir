import Layout from "@/components/Layout.vue";
import type { RouteRecordRaw } from "vue-router";
import BlankView from "../components/BlankView.vue";

const routes: RouteRecordRaw[] = [
  {
    path: "/", component: Layout, children: [
      { path: "/", name: "home", component: () => import("@/views/HomeView.vue"), meta: { title: "Accueil" } },
      { path: "/login", name: "login", component: () => import("@/views/LoginView.vue"), meta: { title: "Connexion" } },
      { path: "/profile", name: "profile", component: () => import("@/views/ProfileView.vue"), meta: { title: "Profil utilisateur", access: [] } },
      { path: "/tasks", name: "tasks", component: () => import("@/views/TasksView.vue"), meta: { title: "Tâches", access: [ "USER" ] } },

      {
        path: "/admin", component: BlankView, children: [
          { path: "/connected_users", name: "admin-connected-users", component: () => import("@/views/admin/ConnectedUsersView.vue"), meta: { title: "Utilisateurs connectés", access: [ "ADMIN" ] } },
          { path: "/swagger", name: "admin-swagger", component: () => import("@/views/admin/SwaggerView.vue"), meta: { title: "API", access: [ "ADMIN" ] } },
        ],
      },

      {
        path: "/dev", component: BlankView, children: [
          { path: "/datatable", name: "dev-datatable", component: () => import("@/views/dev/DevDatatableView.vue"), meta: { title: "DEV - Datatable", access: [ "USER" ] } },
          { path: "/modal", name: "dev-modal", component: () => import("@/views/dev/DevModalView.vue"), meta: { title: "DEV - Modal", access: [ "USER" ] } },
          { path: "/toast", name: "dev-toast", component: () => import("@/views/dev/DevToastView.vue"), meta: { title: "DEV - Toast", access: [ "USER" ] } },
        ],
      },

      {
        path: "/errors", component: BlankView, children: [
          { name: "errors-404", path: "/:catchAll(.*)*", component: () => import("@/views/errors/Error404View.vue"), meta: { no_match: true } },
        ],
      },
    ],
  },
];

export const appMenu: Array<MenuItem> = [
  {
    label: "Menu", children: [
      { label: "Home", route: { name: "home" } },
      { label: "Tasks", route: { name: "tasks" } },
    ],
  },
  {
    label: "Admin", children: [
      { label: "Utilisateurs connectés", route: { name: "admin-connected-users" } },
      { label: "API", route: { name: "admin-swagger" } },
    ],
  },
  {
    label: "DEV", children: [
      { label: "Datatable", route: { name: "dev-datatable" } },
      { label: "Modal", route: { name: "dev-modal" } },
      { label: "Toast", route: { name: "dev-toast" } },
    ],
  },
];

export default routes;
