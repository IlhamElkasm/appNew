import { Component, Input, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { AuthService } from 'src/app/auth/services/auth.service';
import { Formation } from 'src/app/Model/Formation';
import { FormationService } from 'src/app/Service/formation.service';
import { ReservationService } from 'src/app/Service/reservation.service';

@Component({
  selector: 'app-formation-details',
  templateUrl: './formation-details.component.html',
  styleUrls: ['./formation-details.component.css']
})
export class FormationDetailsComponent implements OnInit {
  // formation!: Formation;
   loading = false;
    @Input() formation!: Formation;

  constructor(
    private route: ActivatedRoute,
    private formationService: FormationService,
    public authService: AuthService,
    private toastr: ToastrService,
    private reservationService: ReservationService,
    private router: Router
  ) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.formationService.getFormation(id).subscribe({
      next: (data) => (this.formation = data),
      error: (err) => console.error('Error:', err),
    });
  }


  reserve(formation: Formation): void {
    if (!this.authService.isLoggedIn) {
      this.toastr.warning('Please log in to reserve a formation', 'Authentication Required');
      return;
    }
  
    if (!formation || formation.id === undefined) {
      this.toastr.error('Invalid formation selected.', 'Error');
      return;
    }
  
    this.loading = true;
    this.reservationService.createReservation(formation.id).subscribe({
      next: (response) => {
        this.toastr.success('Reservation submitted successfully', 'Success');
        alert('Reservation submitted successfully');
        this.loading = false;
  
        // ✅ Navigate after success
        this.router.navigate(['/paiement']);
      },
      error: (error) => {
        console.error('Error creating reservation:', error);
  
        let errorMessage = 'Failed to create reservation';
        if (error.error?.message) {
          errorMessage = error.error.message;
        } else if (error.status === 403) {
          errorMessage = 'You do not have permission to make reservations';
        } else if (error.status === 400) {
          errorMessage = 'Invalid formation or no slots available';
        }
  
        this.toastr.error(errorMessage, 'Error');
        this.loading = false;
      }
    });
  }
  goBack(): void {
    this.router.navigate(['/formations']);
  }
  
}
