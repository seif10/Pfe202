import { Routes } from '@angular/router';
import { LogsComponent } from './logs/logs.component';
import { SystemComponent } from './system/system.component';
import { ReportComponent } from './report/report.component';

export const routes: Routes = [
    { path:'logs', component : LogsComponent},
    { path: 'sytem' , component : SystemComponent},
    { path: 'report' , component : ReportComponent}
   // { path: '', redirectTo: '/systemMetrics', pathMatch: 'full' } 
];
