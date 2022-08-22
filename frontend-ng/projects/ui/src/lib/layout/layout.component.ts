import { Component, Input, OnInit } from "@angular/core";
import { BreakpointObserver, BreakpointState } from "@angular/cdk/layout";
import { Title } from "@angular/platform-browser";

interface MenuItem {
  label: string;
  type: "heading" | "item";
  route?: string;
}

@Component({
  selector: "aeg-layout",
  templateUrl: "./layout.component.html",
  styleUrls: [ "./layout.component.css" ],
})
export class LayoutComponent implements OnInit {

  public isMobile!: boolean;
  public opened!: boolean;

  @Input() title!: string;
  @Input() description!: string;

  public menuItems: MenuItem[] = [
    { type: "heading", label: "Menu" },
    { type: "item", label: "Home", route: "/" },
    { type: "item", label: "Tasks", route: "/tasks" },
    { type: "heading", label: "Admin" },
    { type: "item", label: "Utilisateurs connectés", route: "/admin/connected_users" },
    { type: "item", label: "API", route: "/admin/api" },
  ];

  constructor(public breakpointObserver: BreakpointObserver, public titleService: Title) { }

  get isSidebarClosed() {
    return this.isMobile && !this.opened;
  }

  toggle() {
    this.opened = !this.opened;
  }

  ngOnInit(): void {
    this.breakpointObserver.observe(
      "(max-width: 1024px)",
    )
      .subscribe((value: BreakpointState) => {
        if (value.matches) {
          this.opened = false;
        }
        this.isMobile = value.matches;
      });
  }

}
