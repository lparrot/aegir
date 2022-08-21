import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { ViewsRoutingModule } from "./views-routing.module";
import { ViewHomeComponent } from "./view-home/view-home.component";
import { ViewTasksComponent } from "./view-tasks/view-tasks.component";


@NgModule({
  imports: [
    CommonModule,
    ViewsRoutingModule,
  ],
  declarations: [
    ViewHomeComponent,
    ViewTasksComponent,
  ],
})
export class ViewsModule {
}
