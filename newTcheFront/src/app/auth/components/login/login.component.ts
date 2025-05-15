import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { UserRole } from '../../models/user.model';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  loginForm: FormGroup;
  error: string = '';
  loading = false;
  errorMessage: string = '';

  constructor(
    private formBuilder: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.loginForm = this.formBuilder.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required]
    });
  }
 
  onSubmit() {
    if (this.loginForm.invalid) {
      return;
    }
    
    this.loading = true;
    this.error = '';
    
    this.authService.login(this.loginForm.value).subscribe({
      next: (response) => {
        this.loading = false;
        console.log('Utilisateur connecté:', response);
       
        // Obtenir l'utilisateur actuel depuis le service
        const user = this.authService.currentUserValue;
        console.log('Utilisateur actuel:', user);
        
        // Vérifier si l'utilisateur a les rôles ADMIN ou SECRETAIRE
        if (user && (this.authService.isAdmin() || this.authService.isSecretaire())) {
          console.log('Redirection vers dashboardAdmin');
          this.router.navigate(['/dashboardAdmin']);
        } else {
          console.log('Redirection vers home');
          this.router.navigate(['/home']);
        }
      },
      error: (error) => {
        this.loading = false;
        console.error('Erreur de connexion:', error);
        this.errorMessage = 'Échec de la connexion. Veuillez vérifier vos identifiants.';
      }
    });
  }
}