// import { Injectable } from '@angular/core';
// import { HttpClient, HttpErrorResponse } from '@angular/common/http';
// import { Observable, throwError } from 'rxjs';
// import { catchError } from 'rxjs/operators';
// import { Reservation } from '../Model/Reservation';

// @Injectable({
//   providedIn: 'root'
// })
// export class ReservationService {
//   private apiUrl = 'http://localhost:8081/api/reservations/client';

//   constructor(private http: HttpClient) { }

//   createReservation(formationId: number): Observable<Reservation> {
//     return this.http.post<Reservation>(this.apiUrl, { formationId })
//       .pipe(
//         catchError(this.handleError)
//       );
//   }

//   getUserReservations(): Observable<Reservation[]> {
//     return this.http.get<Reservation[]>(`${this.apiUrl}/my-reservations`)
//       .pipe(
//         catchError(this.handleError)
//       );
//   }

//   getMyReservations(): Observable<Reservation[]> {
//     return this.http.get<Reservation[]>(`${this.apiUrl}`)
//       .pipe(
//         catchError(this.handleError)
//       );
//   }
  
//   getReservationById(id: number): Observable<Reservation> {
//     return this.http.get<Reservation>(`${this.apiUrl}/${id}`)
//       .pipe(
//         catchError(this.handleError)
//       );
//   }

//   cancelReservation(id: number): Observable<Reservation> {
//     return this.http.put<Reservation>(`${this.apiUrl}/${id}/status`, { status: 'CANCELLED' })
//       .pipe(
//         catchError(this.handleError)
//       );
//   }

//   getNewReservationCount(): Observable<number> {
//     return this.http.get<number>('http://localhost:8081/api/reservations/client/admin/count/new')
//       .pipe(
//         catchError(this.handleError)
//       );
//   }

//   // Error handling method
//   private handleError(error: HttpErrorResponse) {
//     let errorMessage = 'An unknown error occurred!';
    
//     if (error.error instanceof ErrorEvent) {
//       // Client-side error
//       errorMessage = `Error: ${error.error.message}`;
//     } else {
//       // Server-side error
//       errorMessage = `Error Code: ${error.status}\nMessage: ${error.message}`;
      
//       // More specific error handling
//       switch (error.status) {
//         case 400:
//           errorMessage = 'Invalid request. Please check your input.';
//           break;
//         case 401:
//           errorMessage = 'Unauthorized. Please log in again.';
//           break;
//         case 403:
//           errorMessage = 'You do not have permission to perform this action.';
//           break;
//         case 404:
//           errorMessage = 'The requested resource was not found.';
//           break;
//         case 500:
//           errorMessage = 'Internal server error. Please try again later.';
//           break;
//       }
//     }
    
//     console.error(errorMessage, error);
//     return throwError(() => new Error(errorMessage));
//   }
// }





import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Reservation } from '../Model/Reservation';

@Injectable({
  providedIn: 'root'
})
export class ReservationService {
  private apiUrl = 'http://localhost:8081/api/reservations';
  
  constructor(private http: HttpClient) { }
  
  // createReservation(formationId: number): Observable<Reservation> {
  //   return this.http.post<Reservation>(this.apiUrl, { formationId })
  //     .pipe(
  //       catchError(this.handleError)
  //     );
  // }

  createReservation(formationId: number): Observable<Reservation> {
  console.log('Creating reservation for formation:', formationId);
  return this.http.post<Reservation>(`${this.apiUrl}/client/add`, { formationId })
    .pipe(
      catchError((error: HttpErrorResponse) => {
        console.error('Full error:', error);
        console.error('Error details:', error.error);
        return this.handleError(error);
      })
    );
}
  
  getUserReservations(): Observable<Reservation[]> {
    return this.http.get<Reservation[]>(`${this.apiUrl}/my-reservations`)
      .pipe(
        catchError(this.handleError)
      );
  }
  
  getMyReservations(): Observable<Reservation[]> {
    return this.http.get<Reservation[]>(`${this.apiUrl}`)
      .pipe(
        catchError(this.handleError)
      );
  }
 
  getReservationById(id: number): Observable<Reservation> {
    return this.http.get<Reservation>(`${this.apiUrl}/${id}`)
      .pipe(
        catchError(this.handleError)
      );
  }
  
  cancelReservation(id: number): Observable<Reservation> {
    return this.http.put<Reservation>(`${this.apiUrl}/${id}/status`, { status: 'CANCELLED' })
      .pipe(
        catchError(this.handleError)
      );
  }
  
  // Méthode pour les admins - compte des nouvelles réservations
  getNewReservationCount(): Observable<number> {
  return this.http.get<number>(`${this.apiUrl}/admin/count/new`)
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
          errorMessage = 'Invalid request. Please check your input.';
          break;
        case 401:
          errorMessage = 'Unauthorized. Please log in again.';
          break;
        case 403:
          errorMessage = 'You do not have permission to perform this action.';
          break;
        case 404:
          errorMessage = 'The requested resource was not found.';
          break;
        case 500:
          errorMessage = 'Internal server error. Please try again later.';
          break;
      }
    }
   
    console.error(errorMessage, error);
    return throwError(() => new Error(errorMessage));
  }


  // Updated methods for ReservationService

// Add these methods to your existing ReservationService class

getAllReservations(): Observable<any[]> {
  console.log('Fetching all reservations from:', this.apiUrl);
  return this.http.get<any[]>(this.apiUrl)
    .pipe(
      catchError((error: HttpErrorResponse) => {
        console.error('Error fetching reservations:', error);
        console.error('Error details:', error.error);
        return this.handleError(error);
      })
    );
}

// Alternative method if the above doesn't work - try admin endpoint
getAllReservationsAdmin(): Observable<any[]> {
  console.log('Fetching reservations via admin endpoint');
  return this.http.get<any[]>(`${this.apiUrl}/admin/all`)
    .pipe(
      catchError((error: HttpErrorResponse) => {
        console.error('Error fetching admin reservations:', error);
        return this.handleError(error);
      })
    );
}

validateReservation(id: number): Observable<{message: string, status: string}> {
  console.log('Validating reservation:', id);
  return this.http.put<{message: string, status: string}>(`${this.apiUrl}/${id}/validate`, {})
    .pipe(
      catchError((error: HttpErrorResponse) => {
        console.error('Error validating reservation:', error);
        return this.handleError(error);
      })
    );
}

// Alternative validation method if above doesn't work
updateReservationStatus(id: number, status: string): Observable<any> {
  console.log('Updating reservation status:', id, status);
  return this.http.put(`${this.apiUrl}/${id}/status`, { status })
    .pipe(
      catchError((error: HttpErrorResponse) => {
        console.error('Error updating reservation status:', error);
        return this.handleError(error);
      })
    );
}
}