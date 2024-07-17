import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { SystemMetricsComponent } from './system-metrics/system-metrics.component';

export const routes: Routes = [
  { path: 'systemMetrics', component: SystemMetricsComponent },
  { path: '', redirectTo: '/systemMetrics', pathMatch: 'full' } // Default route
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }