import { Component } from "@angular/core";
import { SessionService } from "./session.service";

@Component({
  selector: "app-root",
  templateUrl: "./app.component.html",
  styleUrls: [ "./app.component.scss" ],
})
export class AppComponent {
  constructor(public session: SessionService) {}

  capitalize(title: string) {
    return title.charAt(0).toUpperCase() + title.substring(1);
  }
}
