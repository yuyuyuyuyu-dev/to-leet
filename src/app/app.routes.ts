import { Routes } from '@angular/router';
import { HomeComponent } from './ui/pages/home/home.component';
import { LicensesComponent } from './ui/pages/licenses/licenses.component';

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
