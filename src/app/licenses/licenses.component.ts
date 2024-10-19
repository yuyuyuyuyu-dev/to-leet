import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {
  License,
  NgxMatThirdPartyLicensesListViewComponent,
} from 'ngx-mat-third-party-licenses-list-view';
import _licenses from '../../../public/third-party-licenses.json';

@Component({
  selector: 'app-licenses',
  standalone: true,
  imports: [RouterOutlet, NgxMatThirdPartyLicensesListViewComponent],
  templateUrl: './licenses.component.html',
  styleUrl: './licenses.component.scss',
})
export class LicensesComponent {
  licenses = _licenses as License[];
}
