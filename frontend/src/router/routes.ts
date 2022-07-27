import { RouteRecordRaw } from "vue-router";

const routes: RouteRecordRaw[] = [
  {
    path: "/", component: () => import("layouts/default.vue"), children: [
      { name: "index", path: "", component: () => import("pages/index.vue"), meta: { access: true } },
      { name: "login", path: "login", component: () => import("pages/login.vue"), meta: { access: true } },
      { name: "dashboard", path: "dashboard", component: () => import("pages/dashboard.vue"), meta: { access: [ "USER" ] } },
      {
        path: "errors", component: () => import("components/BlankPage.vue"), children: [
          { name: "errors-401", path: "access-denied", component: () => import("pages/errors/access-denied.vue") },
        ],
      },
    ],
  },
  { name: "errors-404", path: "/:catchAll(.*)*", component: () => import("pages/errors/not-found.vue"), meta: { no_match: true } },
];

export default routes;
