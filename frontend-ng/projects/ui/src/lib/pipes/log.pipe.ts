import { Pipe, PipeTransform } from "@angular/core";

@Pipe({
  name: "log",
})
export class LogPipe implements PipeTransform {

  transform(value: unknown, ..._args: unknown[]): unknown {
    console.log(value);
    return null;

  }

}
