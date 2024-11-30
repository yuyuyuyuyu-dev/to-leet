import { Pipe, PipeTransform } from '@angular/core';
import { ToLeetService } from '../domain/to-leet.service';

@Pipe({
  name: 'toLeet',
  standalone: true,
})
export class ToLeetPipe implements PipeTransform {
  constructor(private toLeetService: ToLeetService) {}

  transform(value: string): string {
    return this.toLeetService.toLeet(value);
  }
}
