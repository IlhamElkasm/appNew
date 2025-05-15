import { Component, HostListener } from '@angular/core';
import { Router } from '@angular/router';
import { ClientRegistration } from 'src/app/auth/models/ClientRegistration';
import { AuthService } from 'src/app/auth/services/auth.service';
import { NotificationService } from 'src/app/Service/notification.service';
import { ReservationService } from 'src/app/Service/reservation.service';
import { Notification } from 'src/app/Model/Notification';

@Component({
  selector: 'app-dashboard-admin',
  templateUrl: './dashboard-admin.component.html',
  styleUrls: ['./dashboard-admin.component.css']
})
export class DashboardAdminComponent {


  mobileMenuOpen = false;
  activeDropdown: string | null = null;
  sidebarExpanded = true; // Add this property
  clients: ClientRegistration[] = [];
  error: string = '';
  newReservationCount: number = 0


  newClientCount: number = 0;

  constructor(
    public authService: AuthService,
    private router: Router,
    private reservationService: ReservationService,
  
  ) { }


  toggleMobileMenu(): void {
    this.mobileMenuOpen = !this.mobileMenuOpen;

    // Close any open dropdowns when toggling mobile menu
    if (!this.mobileMenuOpen) {
      this.activeDropdown = null;
    }
  }

  toggleDropdown(dropdownName: string): void {
    if (this.activeDropdown === dropdownName) {
      this.activeDropdown = null;
    } else {
      this.activeDropdown = dropdownName;
    }
  }
  logout(): void {
    this.authService.logout();
    this.router.navigate(['/home']);
  }
  toggleSidebar(): void {
    this.sidebarExpanded = !this.sidebarExpanded;
  }

  // Close dropdowns when clicking outside
  @HostListener('document:click', ['$event'])
  onDocumentClick(event: MouseEvent): void {
    const target = event.target as HTMLElement;
    if (!target.closest('.dropdown')) {
      this.activeDropdown = null;
    }
  }

  ngOnInit(): void {
    const currentUser = this.authService.currentUserValue;
    console.log('🧠 Current User:', currentUser);
    console.log('🔐 User Roles:', currentUser?.roles);
    console.log('👑 Is Admin?', this.authService.isAdmin());
    console.log('👑 Is Client?', this.authService.isClient());
    console.log('👑 Is Secretaire?', this.authService.isSecretaire());

    this.authService.getAllClients().subscribe({
      next: (data) => this.clients = data,
      error: (err) => {
        this.error = 'Erreur lors du chargement des clients';
        console.error(err);
      }
    });

    this.loadNewReservationCount();

    this.loadNewClientCount();
  }


  loadNewReservationCount(): void {
    this.reservationService.getNewReservationCount().subscribe({
      next: (count: number) => {
        this.newReservationCount = count;
      },
      error: (err: any) => {
        console.error('Error fetching reservation count:', err);
      },
    });
  }


  loadNewClientCount() {
    this.authService.getNewClientCount().subscribe(count => {
      this.newClientCount = count;
    });
  }
}
