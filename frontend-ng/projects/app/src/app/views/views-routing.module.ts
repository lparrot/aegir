import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";
import { ViewHomeComponent } from "./view-home/view-home.component";
import { ViewTasksComponent } from "./view-tasks/view-tasks.component";

const routes: Routes = [
  { path: "", component: ViewHomeComponent },
  { path: "tasks", component: ViewTasksComponent },
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
