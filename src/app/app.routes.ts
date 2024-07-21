import { Routes } from '@angular/router';
import { LogsComponent } from './logs/logs.component';
import { SystemComponent } from './system/system.component';
import { ReportComponent } from './report/report.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { UserFormComponent } from './user-form/user-form.component';
import { ShowUsersComponent } from './show-users/show-users.component';

export const routes: Routes = [
    { path:'logs', component : LogsComponent},
    { path: 'sytem' , component : SystemComponent},
    { path: 'report' , component : ReportComponent},
    { path: 'dashboard' , component : DashboardComponent},
    { path: 'adduser' , component : UserFormComponent},
    { path: 'showusers' , component : ShowUsersComponent}
   // { path: '', redirectTo: '/systemMetrics', pathMatch: 'full' } 
];
