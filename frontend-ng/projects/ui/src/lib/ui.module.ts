import { NgModule } from "@angular/core";
import { LayoutComponent } from "./layout/layout.component";
import { CommonModule } from "@angular/common";
import { RouterModule } from "@angular/router";
import { LogPipe } from "./pipes/log.pipe";

export const ui_components = [
  LayoutComponent,
  LogPipe,
];

@NgModule({
  declarations: [
    ...ui_components,
  ],
  imports: [
    CommonModule,
    RouterModule,
  ],
  exports: [
    ...ui_components,
  ],
})
export class AegirUiModule {
}
