import { ChangeDetectorRef, Component, Input } from "@angular/core";
import { CdkAccordionItem } from "@angular/cdk/accordion";
import { AccordionComponent } from "../accordion/accordion.component";
import { UniqueSelectionDispatcher } from "@angular/cdk/collections";

@Component({
  selector: "aeg-accordion-item",
  templateUrl: "./accordion-item.component.html",
  styleUrls: [ "./accordion-item.component.scss" ],
})
export class AccordionItemComponent extends CdkAccordionItem {
  @Input() name!: string;
  @Input() title!: string;
  @Input() titleClass!: string;
  @Input() contentClass!: string | null;

  index!: number;
  first!: boolean;
  last!: boolean;

  constructor(accordion: AccordionComponent, detector: ChangeDetectorRef, uniqueSelectionDispatcher: UniqueSelectionDispatcher) {
    super(accordion, detector, uniqueSelectionDispatcher);
  }
}
