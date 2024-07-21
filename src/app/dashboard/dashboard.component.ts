import { Component } from '@angular/core';
import { SystemComponent } from "../system/system.component";
import { LogsComponent } from "../logs/logs.component";

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [SystemComponent, LogsComponent],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent {

}
