import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ParsedLog } from './model/parsed-log.model';

@Injectable({
  providedIn: 'root'
})
export class SystemDataService {

  private apiUrl = 'http://localhost:8999/log';

  constructor(private http: HttpClient) { }

  fetchItem(itemName: string): Observable<number> {
    let params = new HttpParams().set('itemName', itemName);
    return this.http.get<number>(this.apiUrl+'/systemLog', { params });
  }

  getParsedLogs(): Observable<ParsedLog[]> {
    return this.http.get<ParsedLog[]>(this.apiUrl+'/getParsedLog');
  }

  getRemoteLogFilesAndAddOrUpdateZabbixItems(): Observable<string[]> {
    return this.http.get<string[]>(this.apiUrl+'/addOrUpdateItem');
  }

  fetchAndSaveLogsValueByNamesInDB(requestBody: { itemNames: string[], limit: number }): Observable<void> {
    return this.http.post<void>(this.apiUrl+'/addLogs', requestBody);
  }
}
