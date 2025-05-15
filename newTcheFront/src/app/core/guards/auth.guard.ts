import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import { AuthService } from '../../auth/services/auth.service';
import { UserRole } from '../../auth/models/user.model';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {
  constructor(private authService: AuthService, private router: Router) {}

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    // Si l'utilisateur n'est pas connecté, redirigez vers la page de connexion
    if (!this.authService.isLoggedIn) {
      return this.router.createUrlTree(['/home']);
    }

    // Gestion de la route racine avec redirection basée sur le rôle
    if (state.url === '/') {
      console.log('Route racine détectée, vérification du rôle');
      
      if (this.authService.isAdmin() || this.authService.isSecretaire()) {
        console.log('Redirection admin vers dashboardAdmin');
        return this.router.createUrlTree(['/dashboardAdmin']);
      }
      
      if (this.authService.isClient()) {
        console.log('Redirection client vers home');
        return this.router.createUrlTree(['/home']);
      }
    }

    // Vérification des rôles pour les autres routes
    const requiredRoles = route.data?.['roles'] as UserRole[];
    if (requiredRoles && requiredRoles.length > 0) {
      const hasAccess = requiredRoles.some(role => this.authService.hasRole(role));
      if (!hasAccess) {
        console.log('Accès refusé à la route:', state.url);
        return this.router.createUrlTree(['/unauthorized']);
      }
    }

    return true;
  }
}