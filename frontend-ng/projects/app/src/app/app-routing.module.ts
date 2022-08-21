import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";

const routes: Routes = [
  { path: "", loadChildren: () => import("./views/views-routing.module").then(m => m.ViewsRoutingModule) },
];

@NgModule({
  imports: [ RouterModule.forRoot(routes, { enableTracing: true }) ],
  exports: [ RouterModule ],
})
export class AppRoutingModule {
}
