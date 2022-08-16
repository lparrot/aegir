import { Component } from "@angular/core";
import { ApiService } from "./api.service";

@Component({
  selector: "app-root",
  templateUrl: "./app.component.html",
  styles: [],
})
export class AppComponent {
  title = "app";


  constructor(api: ApiService) {
    api.getAppInformations()
      .subscribe(value => {
        console.log(value);
      });
  }
}
