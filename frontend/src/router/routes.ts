import { RouteRecordRaw } from "vue-router";

const routes: RouteRecordRaw[] = [
  {
    path: "/", component: () => import("layouts/default.vue"), children: [
      { path: "", component: () => import("pages/index.vue") },
      { path: "login", component: () => import("pages/login.vue") },
      { path: "dashboard", component: () => import("pages/dashboard.vue") },
    ],
  },

  {
    path: "/:catchAll(.*)*",
    component: () => import("pages/errors/not-found.vue"),
  },
];

export default routes;
