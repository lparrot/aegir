import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";
import { environment } from "../environments/environment";

const routes: Routes = [
  { path: "", loadChildren: () => import("./views/views-routing.module").then(m => m.ViewsRoutingModule) },
];

@NgModule({
  imports: [ RouterModule.forRoot(routes, { enableTracing: environment.logRouter }) ],
  exports: [ RouterModule ],
})
export class AppRoutingModule {
}
