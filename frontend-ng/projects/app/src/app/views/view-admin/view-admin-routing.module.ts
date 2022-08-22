import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";
import { ViewAdminApiComponent } from "./view-admin-api/view-admin-api.component";
import { ViewAdminConnectedUsersComponent } from "./view-admin-connected-users/view-admin-connected-users.component";

const routes: Routes = [
  { path: "api", component: ViewAdminApiComponent, title: "API" },
  { path: "connected_users", component: ViewAdminConnectedUsersComponent, title: "Utilisateurs connectés" },
];

@NgModule({
  declarations: [],
  imports: [
    RouterModule.forChild(routes),
  ],
  exports: [
    RouterModule,
  ],
})
export class ViewAdminRoutingModule {
}
