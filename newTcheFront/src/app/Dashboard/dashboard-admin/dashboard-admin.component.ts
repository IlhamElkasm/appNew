import { Component, HostListener, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { Subscription, interval } from 'rxjs';
import { ClientRegistration } from 'src/app/auth/models/ClientRegistration';
import { AuthService } from 'src/app/auth/services/auth.service';
import { ReservationService } from 'src/app/Service/reservation.service';
import { NotificationService } from 'src/app/Service/notification.service';
import { Notification } from 'src/app/Model/Notification';

@Component({
  selector: 'app-dashboard-admin',
  templateUrl: './dashboard-admin.component.html',
  styleUrls: ['./dashboard-admin.component.css']
})
export class DashboardAdminComponent implements OnInit, OnDestroy {

  sidebarExpanded = true;
  clients: ClientRegistration[] = [];
  error: string = '';
  newReservationCount: number = 0;
  mobileMenuOpen = false;
  activeDropdown: string | null = null;
  newClientCount: number = 0;

  // Notifications
  notifications: Notification[] = [];
  unreadNotificationCount: number = 0;
  private notificationSubscription?: Subscription;

  constructor(
    public authService: AuthService,
    private router: Router,
    private reservationService: ReservationService,
    private notificationService: NotificationService
  ) { }

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
    this.loadNotifications();

    // Refresh notifications every 30 seconds
    this.notificationSubscription = interval(30000).subscribe(() => {
      this.loadNotifications();
    });
  }

  ngOnDestroy(): void {
    if (this.notificationSubscription) {
      this.notificationSubscription.unsubscribe();
    }
  }

  loadNotifications(): void {
    this.notificationService.getUnreadNotifications().subscribe({
      next: (notifications) => {
        this.notifications = notifications;
      },
      error: (error) => {
        console.error('Error loading notifications:', error);
      }
    });

    this.notificationService.getUnreadNotificationCount().subscribe({
      next: (count) => {
        this.unreadNotificationCount = count;
      },
      error: (error) => {
        console.error('Error loading notification count:', error);
      }
    });
  }

  toggleMobileMenu(): void {
    this.mobileMenuOpen = !this.mobileMenuOpen;
    
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

  @HostListener('document:click', ['$event'])
  onDocumentClick(event: MouseEvent): void {
    const target = event.target as HTMLElement;
    if (!target.closest('.dropdown')) {
      this.activeDropdown = null;
    }
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

  // Getter pour que le template puisse utiliser unreadCount
  get unreadCount(): number {
    return this.unreadNotificationCount;
  }

  markAsRead(notification: Notification): void {
    if (notification.id && !notification.isRead) {
      this.notificationService.markAsRead(notification.id).subscribe({
        next: () => {
          notification.isRead = true;
          this.loadNotifications(); // Refresh the count
        },
        error: (error) => {
          console.error('Error marking notification as read:', error);
        }
      });
    }
  }

  markAllAsRead(): void {
    this.notificationService.markAllAsRead().subscribe({
      next: () => {
        this.notifications.forEach(notification => {
          notification.isRead = true;
        });
        this.unreadNotificationCount = 0;
      },
      error: (error) => {
        console.error('Error marking all notifications as read:', error);
      }
    });
  }

  getTimeAgo(date: Date): string {
    const now = new Date();
    const notificationDate = new Date(date);
    const diffInMinutes = Math.floor((now.getTime() - notificationDate.getTime()) / (1000 * 60));
    
    if (diffInMinutes < 1) {
      return 'Just now';
    } else if (diffInMinutes < 60) {
      return `${diffInMinutes} minutes ago`;
    } else if (diffInMinutes < 1440) {
      const hours = Math.floor(diffInMinutes / 60);
      return `${hours} hour${hours > 1 ? 's' : ''} ago`;
    } else {
      const days = Math.floor(diffInMinutes / 1440);
      return `${days} day${days > 1 ? 's' : ''} ago`;
    }
  }
}