// reservation-list.component.ts
import { Component, OnInit } from '@angular/core';
import { ReservationService } from 'src/app/Service/reservation.service';

@Component({
  selector: 'app-reservation-list',
  templateUrl: './reservation-list.component.html',
  styleUrls: ['./reservation-list.component.css']
})
export class ReservationListComponent implements OnInit {
  reservations: any[] = [];
  loading = false;
  error: string | null = null;

  constructor(private reservationService: ReservationService) {}

  ngOnInit(): void {
    this.loadReservations();
  }

  loadReservations(): void {
    this.loading = true;
    this.error = null;
   
    this.reservationService.getAllReservations().subscribe({
      next: (data) => {
        console.log('Raw reservation data:', data); // Debug log
        this.reservations = data;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading reservations:', error);
        this.error = 'Erreur lors du chargement des réservations';
        this.loading = false;
      }
    });
  }

  validate(id: number): void {
    if (confirm('Êtes-vous sûr de vouloir valider cette réservation ?')) {
      this.reservationService.validateReservation(id).subscribe({
        next: () => {
          alert('Réservation validée avec succès!');
          this.loadReservations(); // Refresh list
        },
        error: (error) => {
          console.error('Error validating reservation:', error);
          alert('Erreur lors de la validation de la réservation');
        }
      });
    }
  }

  // Helper methods corrigés pour utiliser les bons noms de champs du DTO
  getClientName(reservation: any): string {
    return reservation?.clientName || 'N/A'; // Utilise directement clientName du DTO
  }

  getFormationTitle(reservation: any): string {
    return reservation?.formationTitle || 'N/A'; // Utilise directement formationTitle du DTO
  }

  getStatusLabel(status: string): string {
    switch(status) {
      case 'PENDING': return 'En attente';
      case 'VALIDATED': return 'Validée';  
      case 'CONFIRMED': return 'Confirmée'; // Ajout de CONFIRMED car votre service utilise ce statut
      case 'CANCELLED': return 'Annulée';
      default: return status;
    }
  }
}