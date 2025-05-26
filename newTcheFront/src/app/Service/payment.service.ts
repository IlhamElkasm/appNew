import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Payment } from '../Model/Payment';

@Injectable({
  providedIn: 'root'
})
export class PaymentService {
  private apiUrl = 'http://localhost:8081/api/payments';

  constructor(private http: HttpClient) { }

  processPayment(reservationId: number, amount: number): Observable<Payment> {
    return this.http.post<Payment>(`${this.apiUrl}/process`, { reservationId, amount })
      .pipe(
        catchError(this.handleError)
      );
  }

  getPaymentByReservationId(reservationId: number): Observable<Payment> {
    return this.http.get<Payment>(`${this.apiUrl}/reservation/${reservationId}`)
      .pipe(
        catchError(this.handleError)
      );
  }

  getAllPayments(): Observable<Payment[]> {
    return this.http.get<Payment[]>(this.apiUrl)
      .pipe(
        catchError(this.handleError)
      );
  }

  updatePaymentStatus(id: number, status: string): Observable<Payment> {
    return this.http.put<Payment>(`${this.apiUrl}/${id}/status`, { status })
      .pipe(
        catchError(this.handleError)
      );
  }

  // Error handling method
  private handleError(error: HttpErrorResponse) {
    let errorMessage = 'An unknown error occurred!';
    
    if (error.error instanceof ErrorEvent) {
      // Client-side error
      errorMessage = `Error: ${error.error.message}`;
    } else {
      // Server-side error
      errorMessage = `Error Code: ${error.status}\nMessage: ${error.message}`;
      
      // More specific error handling
      switch (error.status) {
        case 400:
          errorMessage = 'Invalid payment information. Please check your input.';
          break;
        case 401:
          errorMessage = 'Unauthorized. Please log in again.';
          break;
        case 403:
          errorMessage = 'You do not have permission to perform this action.';
          break;
        case 404:
          errorMessage = 'The payment or reservation was not found.';
          break;
        case 500:
          errorMessage = 'Internal server error. Please try again later.';
          break;
      }
    }
    
    console.error(errorMessage, error);
    return throwError(() => new Error(errorMessage));
  }
}