import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { AuthService } from 'src/app/auth/services/auth.service';
import { Formation } from 'src/app/Model/Formation';
import { FormationService } from 'src/app/Service/formation.service';
import { ReservationService } from 'src/app/Service/reservation.service';

@Component({
  selector: 'app-formation-detail2',
  templateUrl: './formation-detail2.component.html',
  styleUrls: ['./formation-detail2.component.css']
})
export class FormationDetail2Component implements OnInit {
  formation!: Formation;
  loading = false;

  constructor(
    private route: ActivatedRoute,
    private formationService: FormationService,
    public authService: AuthService,
    private toastr: ToastrService,
    private reservationService: ReservationService,
    public router: Router
  ) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.formationService.getFormation(id).subscribe({
      next: (data) => this.formation = data,
      error: (err) => console.error('Error:', err),
    });
  }

  reserve(formation: Formation): void {
    if (!this.authService.isLoggedIn) {
      this.toastr.warning('Please log in to reserve a formation', 'Authentication Required');
      this.router.navigate(['/login']); // Redirect to login page
      return;
    }

    if (!this.authService.isClient()) {
      this.toastr.warning('Only clients can reserve a formation. Please create an account or log in as a client.', 'Client Required');
      this.router.navigate(['/signup']); // Redirect to signup page
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
        this.router.navigate(['/paiement']);
      },
      error: (error) => {
        console.error('Error creating reservation:', error);
        this.toastr.error('Failed to create reservation', 'Error');
        this.loading = false;
      }
    });
  }

  goBack(): void {
    this.router.navigate(['/formations']);
  }
}
