import { Injectable } from "@angular/core";

@Injectable({
  providedIn: "root",
})
export class SessionService {

  public configuration!: any;
  public user!: Map<string, any>;

  constructor() { }

}
