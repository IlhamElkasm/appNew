// src/app/auth/components/register-client/register-client.component.ts
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-register-client',
  templateUrl: './register-client.component.html',
  styleUrls: ['./register-client.component.css']
})
export class RegisterClientComponent {
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
      ]],
      telephone: ['', [Validators.required]],
      adresse: ['']
    });
  }
  
  onSubmit() {
    if (this.registerForm.invalid) {
      return;
    }
    
    this.loading = true;
    this.error = '';
    
    this.authService.registerClient(this.registerForm.value).subscribe({
      next: () => {
        this.loading = false;
        this.router.navigate(['/auth/login'], { 
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
