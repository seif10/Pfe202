import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from './model/user.model';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private apiUrl = 'http://localhost:8999/users'; // Adjust the URL as needed

  constructor(private http: HttpClient) {}

  addUser(user: User): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/adduser`, user);
  }

  deleteUser(username: string): Observable<any> {
    const url = `${this.apiUrl}/delete/${username}`;
    return this.http.delete(url, { responseType: 'text' });
  }

  getAllUsers(): Observable<User[]> {
    return this.http.get<User[]>(`${this.apiUrl}/showAll`);
  }
}
