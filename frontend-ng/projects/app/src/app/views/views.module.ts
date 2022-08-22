import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { ViewsRoutingModule } from "./views-routing.module";
import { ViewHomeComponent } from "./view-home/view-home.component";
import { ViewTasksComponent } from "./view-tasks/view-tasks.component";
import { ViewLoginComponent } from "./view-login/view-login.component";


@NgModule({
  imports: [
    CommonModule,
    ViewsRoutingModule,
  ],
  declarations: [
    ViewHomeComponent,
    ViewTasksComponent,
    ViewLoginComponent,
  ],
})
export class ViewsModule {
}
