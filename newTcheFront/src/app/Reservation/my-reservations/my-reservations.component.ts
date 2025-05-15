// src/app/reservations/my-reservations/my-reservations.component.ts
import { Component, OnInit } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { Reservation } from 'src/app/Model/Reservation';
import { ReservationStatus } from 'src/app/Model/ReservationStatus';
import { ReservationService } from 'src/app/Service/reservation.service';

@Component({
  selector: 'app-my-reservations',
  templateUrl: './my-reservations.component.html',
  styleUrls: ['./my-reservations.component.css']
})
export class MyReservationsComponent implements OnInit {
  reservations: Reservation[] = [];
  loading = false;
  statusClasses = {
    [ReservationStatus.PENDING]: 'status-pending',
    [ReservationStatus.CONFIRMED]: 'status-confirmed',
    [ReservationStatus.CANCELLED]: 'status-cancelled',
    [ReservationStatus.COMPLETED]: 'status-completed'
  };

  constructor(
    private reservationService: ReservationService,
    private toastr: ToastrService
  ) { }

  ngOnInit(): void {
    this.reservationService.getMyReservations().subscribe({
      next: (data) => this.reservations = data,
      error: (err) => console.error('Error fetching reservations', err)
    });
    this.loadReservations();
  }

  loadReservations(): void {
    this.loading = true;
    this.reservationService.getUserReservations().subscribe({
      next: (data) => {
        this.reservations = data;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading reservations:', error);
        this.toastr.error('Failed to load your reservations', 'Error');
        this.loading = false;
      }
    });
  }

  cancelReservation(id?: number): void {
    if (id == null) {
      this.toastr.error('Invalid reservation ID', 'Error');
      return;
    }
  
    if (confirm('Are you sure you want to cancel this reservation?')) {
      this.reservationService.cancelReservation(id).subscribe({
        next: () => {
          this.toastr.success('Reservation cancelled successfully', 'Success');
          this.loadReservations();
        },
        error: (error) => {
          console.error('Error cancelling reservation:', error);
          this.toastr.error('Failed to cancel reservation', 'Error');
        }
      });
    }
  }
  

  getStatusClass(status: ReservationStatus): string {
    return this.statusClasses[status] || 'status-unknown';
  }
  
}