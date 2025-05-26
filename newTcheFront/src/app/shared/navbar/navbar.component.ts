import { Component, HostListener } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../auth/services/auth.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent {
  mobileMenuOpen = false;
  activeDropdown: string | null = null;
  
  constructor(
    public authService: AuthService,
    private router: Router
  ) {}
  
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
  
  // Close dropdowns when clicking outside
  @HostListener('document:click', ['$event'])
  onDocumentClick(event: MouseEvent): void {
    const target = event.target as HTMLElement;
    if (!target.closest('.dropdown')) {
      this.activeDropdown = null;
    }
  }

  ngOnInit() {
  // Log existant
  const currentUser = this.authService.currentUserValue;
  console.log('🧠 Current User:', currentUser);
  console.log('👤 User nom:', currentUser?.nom);
  console.log('🔐 User Roles:', currentUser?.roles);
  console.log('👑 Is Admin?', this.authService.isAdmin());
  console.log('👑 Is Client?', this.authService.isClient());
  console.log('👑 Is Secretaire?', this.authService.isSecretaire());
  
  // Vérification supplémentaire pour le nom
  if (!currentUser?.nom) {
    console.warn('⚠️ User nom is empty or undefined!');
    // Tentative de récupération depuis localStorage
    const userJson = localStorage.getItem('user');
    if (userJson) {
      const storedUser = JSON.parse(userJson);
      console.log('📦 User from localStorage:', storedUser);
      console.log('👤 Nom from localStorage:', storedUser.nom);
    }
  }
}
  
  
}