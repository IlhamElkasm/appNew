import { Injectable } from '@angular/core';
import { Router, Resolve, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { Observable } from 'rxjs';
import { AuthService } from '../../auth/services/auth.service';
import { UserRole } from '../../auth/models/user.model';

@Injectable({
  providedIn: 'root'
})
export class RedirectResolver implements Resolve<boolean> {
  constructor(private authService: AuthService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> | Promise<boolean> | boolean {
    console.log('RedirectResolver est exécuté');
    // Vérifiez si l'utilisateur est connecté
    if (!this.authService.isLoggedIn) {
      this.router.navigate(['/home']);
      return false;
    }

    // Vérifiez le rôle de l'utilisateur et redirigez en conséquence
    if (this.authService.isAdmin() || this.authService.isSecretaire()) {
      console.log('Redirection vers /dashboardAdmin');
      this.router.navigate(['/dashboardAdmin']);
    } else if (this.authService.isClient()) {
      console.log('Redirection vers /home');
      this.router.navigate(['/home']);
    } else {
      // Redirection par défaut
      console.log('Redirection par défaut vers /home');
      this.router.navigate(['/home']);
    }
    
    return true;
  }
}