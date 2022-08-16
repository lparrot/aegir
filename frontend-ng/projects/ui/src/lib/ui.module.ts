import { NgModule } from "@angular/core";
import { LayoutComponent } from "./layout/layout.component";

export const ui_components = [
  LayoutComponent,
];

@NgModule({
  declarations: [
    ...ui_components,
  ],
  imports: [],
  exports: [
    ...ui_components,
  ],
})
export class AegirUiModule {
}
