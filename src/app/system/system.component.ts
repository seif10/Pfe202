import { Component } from '@angular/core';
import { GrafanaService } from '../grafana.service';
import { JsonPipe, NgFor, NgIf } from '@angular/common';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import { animate, state, style, transition, trigger } from '@angular/animations';
import {MatIconModule} from '@angular/material/icon';

@Component({
  selector: 'app-system',
  standalone: true,
  imports: [NgIf,JsonPipe,NgFor,MatIconModule],
  templateUrl: './system.component.html',
  styleUrl: './system.component.css',
  animations: [
    trigger('rotateIcon', [
      state('open', style({
        transform: 'rotate(180deg)'
      })),
      state('closed', style({
        transform: 'rotate(0)'
      })),
      transition('open <=> closed', animate('200ms ease-in-out'))
    ])
  ]
})
export class SystemComponent {
  Cpu_memory_IframeUrl!: SafeResourceUrl; 
  space_utilisation_IframeUrl!: SafeResourceUrl;

  grafanaIframeUrls: SafeResourceUrl[] = [];
  DiskIframeUrls: SafeResourceUrl[] = [];

  showDashboards = true;
  ShowDiskPanel = true; // Initial state to show the Grafana dashboards
  iconState = 'open';
  
  constructor(
    private sanitizer: DomSanitizer,
    private grafanaService: GrafanaService
  ) {}

  ngOnInit() {
    this.fetchPanelUrls(['5', '52','4']);
    this.fetchMemoryCpu('9');
    this.fetchSpaceUtilisation('15');
    this.fetchDiskPanelUrls(['89','23','32']); 
    
  }

  fetchPanelUrls(panelIds: string[]) {
    this.grafanaService.fetchPanelUrls(panelIds).subscribe(
      (urls) => {
        this.grafanaIframeUrls = urls.map(url => this.sanitizer.bypassSecurityTrustResourceUrl(url));
        console.log('Grafana Panel URLs:', this.grafanaIframeUrls);
      },
      (error) => {
        console.error('Error fetching Grafana panel URLs:', error);
      }
    );
  }

  fetchDiskPanelUrls(panelIds: string[]) {
    this.grafanaService.fetchPanelUrls(panelIds).subscribe(
      (urls) => {
        this.DiskIframeUrls = urls.map(url => this.sanitizer.bypassSecurityTrustResourceUrl(url));
        console.log('Grafana Panel URLs:', this.DiskIframeUrls);
      },
      (error) => {
        console.error('Error fetching Grafana panel URLs:', error);
      }
    );
  }

  fetchMemoryCpu(panelId:string) {

    this.grafanaService.fetchPanelUrl(panelId).subscribe(
      (url) => {
        this.Cpu_memory_IframeUrl = this.sanitizer.bypassSecurityTrustResourceUrl(url);
        console.log('Grafana Panel URL:', this.Cpu_memory_IframeUrl);
      },
      (error) => {
        console.error('Error fetching Grafana panel URL:', error);
      }
    );
  }

  fetchSpaceUtilisation(panelId:string) {

    this.grafanaService.fetchPanelUrl(panelId).subscribe(
      (url) => {
        this.space_utilisation_IframeUrl = this.sanitizer.bypassSecurityTrustResourceUrl(url);
        console.log('space utilisation :', this.space_utilisation_IframeUrl);
      },
      (error) => {
        console.error('Error fetching Grafana panel URL "space utilisation":', error);
      }
    );
  }

  toggleDashboard(): void {
    this.showDashboards = !this.showDashboards; // Toggles the visibility of Grafana dashboards
    this.iconState = this.showDashboards ? 'open' : 'closed';
  } 

  toggleDashboard2(): void {
    this.ShowDiskPanel = !this.ShowDiskPanel; // Toggles the visibility of Grafana dashboards
    this.iconState = this.ShowDiskPanel ? 'open' : 'closed';
  }
}
