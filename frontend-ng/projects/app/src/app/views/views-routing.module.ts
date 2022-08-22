import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";
import { ViewHomeComponent } from "./view-home/view-home.component";
import { ViewTasksComponent } from "./view-tasks/view-tasks.component";
import { ViewLoginComponent } from "./view-login/view-login.component";

const routes: Routes = [
  { path: "", component: ViewHomeComponent, title: "Accueil", data: { title: "Accueil" } },
  { path: "login", component: ViewLoginComponent, title: "Connexion" },
  { path: "tasks", component: ViewTasksComponent, title: "Tâches" },
  { path: "admin", loadChildren: () => import("./view-admin/view-admin.module").then(m => m.ViewAdminModule) },
];

@NgModule({
  imports: [
    RouterModule.forChild(routes),
  ],
  exports: [
    RouterModule,
  ],
})
export class ViewsRoutingModule {
}
