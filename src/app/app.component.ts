import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { LogsComponent } from './logs/logs.component';
import { SystemComponent } from "./system/system.component";
import { DashboardComponent } from "./dashboard/dashboard.component";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, LogsComponent, SystemComponent, DashboardComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'monitoring-agent-frontend';
}
