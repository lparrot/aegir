import { NgModule } from "@angular/core";
import { LayoutComponent } from "./layout/layout.component";
import { CommonModule } from "@angular/common";
import { RouterModule } from "@angular/router";
import { LogPipe } from "./pipes/log.pipe";
import { CdkAccordionModule } from "@angular/cdk/accordion";
import { AccordionComponent } from "./accordion/accordion.component";
import { AccordionItemComponent } from "./accordion-item/accordion-item.component";
import { RemoveWrapperDirective } from "./directives/remove-wrapper.directive";

export const ui_components = [
  AccordionComponent,
  AccordionItemComponent,
  LayoutComponent,
  RemoveWrapperDirective,
  LogPipe,
];

@NgModule({
  declarations: [
    ...ui_components,

  ],
  imports: [
    CommonModule,
    RouterModule,
    CdkAccordionModule,
  ],
  exports: [
    ...ui_components,
  ],
})
export class AegirUiModule {
}
