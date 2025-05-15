import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, catchError, tap } from 'rxjs';
import { Notification } from 'src/app/Model/Notification';

@Injectable({
  providedIn: 'root',
})
export class NotificationService {
  private apiUrl = 'http://localhost:8081/api/notifications';

  constructor(private http: HttpClient) {}

  getUserNotifications(): Observable<Notification[]> {
    console.log('Calling API endpoint:', `${this.apiUrl}/me`);
    return this.http.get<Notification[]>(`${this.apiUrl}/me`).pipe(
      tap(data => console.log('Received notifications:', data)),
      catchError(error => {
        console.error('Error fetching notifications:', error);
        throw error;
      })
    );
  }
}
