import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class LogReportService {

  private apiUrl = 'http://localhost:8999/report'; // Update with your actual API endpoint

  constructor(private http: HttpClient) {}

  exportLogReport(component: string): Observable<string> {
    return this.http.post<string>(this.apiUrl+'/export-par-thread', component);
  }
}
