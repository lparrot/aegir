import { Injectable } from "@angular/core";
import { ApiService } from "./api.service";
import { SessionService } from "./session.service";
import { lastValueFrom } from "rxjs";

@Injectable({
  providedIn: "root",
})
export class StartupService {

  constructor(private api: ApiService, private session: SessionService) { }

  async init() {
    const { success, result } = await lastValueFrom(this.api.getAppInformations());

    if (success) {
      this.session.configuration = result;
    }
  }
}
