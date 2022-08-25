import type { RouteRecordRaw } from "vue-router";
import Layout from "@/components/Layout.vue";
import BlankView from "../components/BlankView.vue";

const routes: RouteRecordRaw[] = [
  {
    path: "/", component: Layout, children: [
      { path: "/", name: "home", component: () => import("@/views/HomeView.vue"), meta: { title: "Accueil" } },
      { path: "/login", name: "login", component: () => import("@/views/LoginView.vue"), meta: { title: "Connexion" } },
      { path: "/profile", name: "profile", component: () => import("@/views/ProfileView.vue"), meta: { title: "Profil utilisateur" } },
      { path: "/tasks", name: "tasks", component: () => import("@/views/TasksView.vue"), meta: { title: "Tâches" } },

      {
        path: "/admin", component: BlankView, children: [
          { path: "/connected_users", name: "admin-connected-users", component: () => import("@/views/admin/ConnectedUsersView.vue"), meta: { title: "Utilisateurs connectés" } },
          { path: "/swagger", name: "admin-swagger", component: () => import("@/views/admin/SwaggerView.vue"), meta: { title: "API" } },
        ],
      },

      {
        path: "/dev", component: BlankView, children: [
          { path: "/datatable", name: "dev-datatable", component: () => import("@/views/dev/DevDatatableView.vue"), meta: { title: "DEV - Datatable" } },
          { path: "/modal", name: "dev-modal", component: () => import("@/views/dev/DevModalView.vue"), meta: { title: "DEV - Modal" } },
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

export default routes;
