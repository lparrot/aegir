import { APP_INITIALIZER, NgModule } from "@angular/core";
import { BrowserModule } from "@angular/platform-browser";
import { AppRoutingModule } from "./app-routing.module";
import { AppComponent } from "./app.component";
import { HttpClientModule } from "@angular/common/http";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { LayoutModule } from "@angular/cdk/layout";
import { AegirUiModule } from "../../../ui/src/lib/ui.module";
import { StartupService } from "./startup.service";

@NgModule({
  declarations: [
    AppComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    BrowserAnimationsModule,
    AegirUiModule,
    LayoutModule,
  ],
  providers: [
    {
      provide: APP_INITIALIZER,
      useFactory: function(startupService: StartupService) {
        return async () => await startupService.init();
      },
      deps: [ StartupService ],
      multi: true,
    },
  ],
  bootstrap: [ AppComponent ],
})
export class AppModule {
}
