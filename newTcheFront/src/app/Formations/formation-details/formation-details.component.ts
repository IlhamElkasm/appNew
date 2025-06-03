import { Component, Input, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { Subscription } from 'rxjs';
import { AuthService } from 'src/app/auth/services/auth.service';
import { Formation } from 'src/app/Model/Formation';
import { FormationService } from 'src/app/Service/formation.service';
import { ReservationService } from 'src/app/Service/reservation.service';

@Component({
  selector: 'app-formation-details',
  templateUrl: './formation-details.component.html',
  styleUrls: ['./formation-details.component.css']
})
export class FormationDetailsComponent implements OnInit, OnDestroy {
  @Input() formation!: Formation;
  loading = false;
  reservationLoading = false;
  private subscriptions: Subscription[] = [];

  constructor(
    private route: ActivatedRoute,
    private formationService: FormationService,
    public authService: AuthService,
    private toastr: ToastrService,
    private reservationService: ReservationService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadFormation();
  }

  ngOnDestroy(): void {
    // Clean up subscriptions to prevent memory leaks
    this.subscriptions.forEach(sub => sub.unsubscribe());
  }

  private loadFormation(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    
    if (!id || isNaN(id)) {
      this.toastr.error('ID de formation invalide', 'Erreur');
      this.router.navigate(['/formations']);
      return;
    }

    this.loading = true;
    const subscription = this.formationService.getFormation(id).subscribe({
      next: (data) => {
        this.formation = data;
        this.loading = false;
      },
      error: (err) => {
        console.error('Error loading formation:', err);
        this.loading = false;
        this.toastr.error('Erreur lors du chargement de la formation', 'Erreur');
        this.router.navigate(['/formations']);
      }
    });
    
    this.subscriptions.push(subscription);
  }

  reserve(formation: Formation): void {
    // Check authentication
    if (!this.authService.isLoggedIn) {
      this.toastr.warning('Veuillez vous connecter pour réserver', 'Connexion requise');
      this.router.navigate(['/login'], { 
        queryParams: { returnUrl: this.router.url } 
      });
      return;
    }

    // Validate formation
    if (!formation || formation.id === undefined) {
      this.toastr.error('Formation invalide.', 'Erreur');
      return;
    }

    // Prevent multiple clicks
    if (this.reservationLoading) {
      return;
    }

    this.reservationLoading = true;
    
    const subscription = this.reservationService.createReservation(formation.id).subscribe({
      next: (response) => {
        this.reservationLoading = false;
        this.toastr.success('Réservation réussie !', 'Succès');
        
        if (response?.id) {
          
        } else {
          this.router.navigate(['/reservations']);
        }
      },
      error: (error) => {
        this.reservationLoading = false;
        console.error('Reservation error:', error);
        
        let message = 'Erreur de réservation';
        
        // Handle specific error cases
        if (error.status === 401) {
          message = 'Session expirée. Veuillez vous reconnecter.';
          this.authService.logout();
          this.router.navigate(['/login']);
        } else if (error.status === 409) {
          message = 'Vous avez déjà réservé cette formation.';
        } else if (error.error?.message) {
          message = error.error.message;
        } else if (error.status === 0) {
          message = 'Erreur de connexion. Vérifiez votre connexion internet.';
        }
        
        this.toastr.error(message, 'Erreur');
      }
    });
    
    this.subscriptions.push(subscription);
  }

  goBack(): void {
    // Use browser history if available, otherwise navigate to formations
    if (window.history.length > 1) {
      window.history.back();
    } else {
      this.router.navigate(['/formations']);
    }
  }

  // Helper method to check if formation is available
  isFormationAvailable(): boolean {
    return this.formation && !this.loading;
  }

  // Helper method to format price
  getFormattedPrice(): string {
    if (!this.formation?.price) return '';
    return `${this.formation.price.toLocaleString('fr-MA')} DH`;
  }
}