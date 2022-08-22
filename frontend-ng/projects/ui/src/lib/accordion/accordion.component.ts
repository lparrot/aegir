import { Component, ContentChildren, Input, QueryList } from "@angular/core";
import { CdkAccordion } from "@angular/cdk/accordion";
import { AccordionItemComponent } from "../accordion-item/accordion-item.component";

@Component({
  selector: "aeg-accordion",
  templateUrl: "./accordion.component.html",
  styleUrls: [ "./accordion.component.scss" ],
})
export class AccordionComponent extends CdkAccordion {

  @Input() titleClass!: string;
  @Input() contentClass!: string;

  @ContentChildren(AccordionItemComponent) items!: QueryList<AccordionItemComponent>;

  ngAfterContentInit() {
    this.items.forEach((item, index) => {
      item.index = index;
      item.first = index == 0;
      item.last = index == this.items.length - 1;
      item.titleClass = item.titleClass ?? this.titleClass;
      item.contentClass = item.contentClass ?? this.contentClass;
    });
  }

}
