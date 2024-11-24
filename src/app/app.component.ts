import { Component } from '@angular/core';
import { RouterLink, RouterOutlet } from '@angular/router';
import { NgxMatMySimpleAppbarComponent } from '@yu-ko-ba/ngx-mat-my-simple-appbar';

@Component({
    selector: 'app-root',
    imports: [RouterLink, RouterOutlet, NgxMatMySimpleAppbarComponent],
    templateUrl: './app.component.html',
    styleUrl: './app.component.scss'
})
export class AppComponent {
  title = 'to-leet';
}
