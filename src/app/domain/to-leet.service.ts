import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class ToLeetService {
  public toLeet(str: string): string {
    return str
      .split('')
      .map((s: string) => {
        switch (s) {
          case 'A':
          case 'a':
            return '4';
          case 'E':
          case 'e':
            return '3';
          case 'I':
          case 'i':
            return '1';
          case 'O':
          case 'o':
            return '0';
          default:
            return s;
        }
      })
      .join('');
  }
}
