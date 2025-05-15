// src/app/auth/components/register-secretaire/register-secretaire.component.ts
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { UserRole } from '../../models/user.model';

@Component({
  selector: 'app-register-secretaire',
  templateUrl: './register-secretaire.component.html',
  styleUrls: ['./register-secretaire.component.css']
})
export class RegisterSecretaireComponent implements OnInit {
  registerForm: FormGroup;
  error: string = '';
  loading = false;
  
  constructor(
    private formBuilder: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.registerForm = this.formBuilder.group({
      nom: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(50)]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [
        Validators.required, 
        Validators.minLength(8),
        Validators.pattern(/^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\S+$).{8,}$/)
      ]]
    });
  }
  
  ngOnInit() {
    // Check if user is admin
    if (!this.authService.isAdmin()) {
      this.router.navigate(['/unauthorized']);
    }
  }
  
  onSubmit() {
    if (this.registerForm.invalid) {
      return;
    }
    
    this.loading = true;
    this.error = '';
    
    this.authService.registerSecretaire(this.registerForm.value).subscribe({
      next: () => {
        this.loading = false;
        this.router.navigate(['/admin/secretaires'], { 
          queryParams: { registered: true } 
        });
      },
      error: (err) => {
        this.loading = false;
        if (err.status === 409) {
          this.error = 'Email already exists';
        } else {
          this.error = 'Registration failed. Please try again';
        }
        console.error(err);
      }
    });
  }
}
