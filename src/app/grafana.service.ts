import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class GrafanaService {

  private grafanaBaseUrl = 'http://192.168.33.10:3000'; // Base URL of your Grafana instance

  constructor() {}

 // Method to fetch the Iframe URL for a specific panel
 fetchPanelUrl(panelId: string): Observable<string> {
  // Construct the panel URL
    const panelUrl = `${this.grafanaBaseUrl}/d-solo/41URQF7mz/zabbix-full-server-status?orgId=1&refresh=30s&var-Host=serveur%20palmera&panelId=${panelId}&theme=light`;
    return of(panelUrl);
  }

  // Method to fetch the Iframe URLs for multiple panels
  fetchPanelUrls(panelIds: string[]): Observable<string[]> {
    const panelUrls = panelIds.map(panelId => 
      `${this.grafanaBaseUrl}/d-solo/41URQF7mz/zabbix-full-server-status?orgId=1&refresh=30s&var-Host=serveur%20palmera&panelId=${panelId}&theme=light`
    );
    return of(panelUrls);
  }

}
