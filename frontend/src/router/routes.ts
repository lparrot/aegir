import Layout from "@/components/Layout.vue";
import LayoutLandscape from "src/components/LayoutLandscape.vue";
import type { RouteRecordRaw } from "vue-router";
import BlankView from "../components/BlankView.vue";

const routes: RouteRecordRaw[] = [
  {
    path: "/", component: LayoutLandscape, children: [
      { path: "", name: "home", component: () => import("@/views/HomeView.vue"), meta: { landing_page: true } },
      { path: "login", name: "login", component: () => import("@/views/LoginView.vue"), meta: { landing_page: true } },
    ],
  },
  {
    path: "/", component: Layout, children: [
      { path: "profile", name: "profile", component: () => import("@/views/ProfileView.vue"), meta: { title: "Profil utilisateur", access: [] } },
      { path: "tasks", name: "tasks", components: { default: () => import("@/views/tasks/TasksView.vue"), sidebar: () => import("@/components/WorkspaceSidebarTasks.vue") }, meta: { title: "Tâches", access: [ "USER" ], hideTitle: true } },
      { path: "generate_api/:table?", name: "generate_api", components: { default: () => import("@/views/ApiView.vue"), sidebar: () => import("@/components/WorkspaceSidebarApi.vue") }, meta: { title: "Generation d'API", access: [ "USER" ] } },
      { path: "admin/connected_users", name: "admin-connected-users", component: () => import("@/views/admin/ConnectedUsersView.vue"), meta: { title: "Utilisateurs connectés", access: [ "ADMIN" ] } },
      { path: "admin/swagger", name: "admin-swagger", component: () => import("@/views/admin/SwaggerView.vue"), meta: { title: "API", access: [ "ADMIN" ] } },
      { path: "admin/filemanager", name: "admin-filemanager", components: { default: () => import("@/views/admin/FileManager.vue"), sidebar: () => import("@/components/SidebarContentFileManager.vue") }, meta: { title: "Gestionnaire de fichiers", access: [ "ADMIN" ] } },

      { path: "dev/accordion", name: "dev-accordion", component: () => import("@/views/dev/DevAccordionView.vue"), meta: { title: "DEV - Accordion", access: [ "USER" ] } },
      { path: "dev/datatable", name: "dev-datatable", component: () => import("@/views/dev/DevDatatableView.vue"), meta: { title: "DEV - Datatable", access: [ "USER" ] } },
      { path: "dev/loader", name: "dev-loader", component: () => import("@/views/dev/DevLoaderView.vue"), meta: { title: "DEV - Loader", access: [ "USER" ] } },
      { path: "dev/modal", name: "dev-modal", component: () => import("@/views/dev/DevModalView.vue"), meta: { title: "DEV - Modal", access: [ "USER" ] } },
      { path: "dev/toast", name: "dev-toast", component: () => import("@/views/dev/DevToastView.vue"), meta: { title: "DEV - Toast", access: [ "USER" ] } },
      { path: "dev/tree", name: "dev-tree", component: () => import("@/views/dev/DevTreeView.vue"), meta: { title: "DEV - Tree", access: [ "USER" ] } },

      { name: "errors/errors-401", path: "/errors/access_denied", component: () => import("@/views/errors/Error401View.vue") },
    ],
  },
  {
    path: "/", component: BlankView, children: [
      { name: "errors/errors-404", path: "/:catchAll(.*)*", component: () => import("@/views/errors/Error404View.vue"), meta: { no_match: true } },
    ],
  },
];

export default routes;
