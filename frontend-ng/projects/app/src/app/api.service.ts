import { Injectable } from "@angular/core";
import { HttpClient as HttpClientNg } from "@angular/common/http";
import { HttpClient, RestApplicationClient, RestResponse } from "../../../../.generated/rest";
import { Observable } from "rxjs";

@Injectable({
  providedIn: "root",
})
export class ApiService extends RestApplicationClient {

  constructor(public http: HttpClientNg) {
    super(new ApiHttpClient(http));
  }

}

class ApiHttpClient implements HttpClient {
  constructor(private http: HttpClientNg) {}

  request<R>(requestConfig: { method: string; url: string; queryParams?: any; data?: any; copyFn?: (data: R) => R }): RestResponse<R> {
    return new Observable(subscriber => {
      this.http.request<R>(requestConfig.method, requestConfig.url, {
        body: requestConfig.data,
        params: requestConfig.queryParams,
      })
        .subscribe(value => {
          subscriber.next(value);
          subscriber.complete();
        });
    });
  }
}
