import { Routes } from '@angular/router';
import { LicensesComponent } from './licenses/licenses.component';
import { HomeComponent } from './home/home.component';

export const routes: Routes = [
  {
    path: '',
    title: 'ToLeet',
    component: HomeComponent,
  },
  {
    path: 'third-party-licenses',
    title: 'サードパーティライセンス',
    component: LicensesComponent,
  },
];
