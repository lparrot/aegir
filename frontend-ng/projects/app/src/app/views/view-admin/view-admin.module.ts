import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { ViewAdminRoutingModule } from "./view-admin-routing.module";
import { ViewAdminApiComponent } from "./view-admin-api/view-admin-api.component";
import { ViewAdminConnectedUsersComponent } from "./view-admin-connected-users/view-admin-connected-users.component";

@NgModule({
  declarations: [
    ViewAdminApiComponent,
    ViewAdminConnectedUsersComponent,
  ],
  imports: [
    CommonModule,
    ViewAdminRoutingModule,
  ],
})
export class ViewAdminModule {
}
