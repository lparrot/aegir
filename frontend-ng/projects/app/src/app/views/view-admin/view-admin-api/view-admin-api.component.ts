import { Component, OnInit } from "@angular/core";
import { DomSanitizer } from "@angular/platform-browser";

@Component({
  selector: "app-view-admin-api",
  templateUrl: "./view-admin-api.component.html",
  styleUrls: [ "./view-admin-api.component.scss" ],
})
export class ViewAdminApiComponent implements OnInit {

  constructor(private readonly sanitizer: DomSanitizer) { }

  get swaggerPage() {
    return this.sanitizer.bypassSecurityTrustResourceUrl("/swagger-ui/index.html");
  }

  ngOnInit(): void {
  }

}
