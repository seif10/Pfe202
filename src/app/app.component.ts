import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { LogsComponent } from './logs/logs.component';
import { SystemComponent } from "./system/system.component";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, LogsComponent, SystemComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'monitoring-agent-frontend';
}
