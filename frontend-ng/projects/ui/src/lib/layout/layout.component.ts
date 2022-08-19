import { Component, Input, OnInit } from "@angular/core";

@Component({
  selector: "aeg-layout",
  templateUrl: "./layout.component.html",
  styleUrls: [ "./layout.component.css" ],
})
export class LayoutComponent implements OnInit {

  @Input() title!: string;
  @Input() description!: string;

  constructor() { }

  ngOnInit(): void {
  }

}
