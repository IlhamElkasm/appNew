// register-client.component.ts
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

  countries = [
    { name: 'Maroc', code: '+212', flag: '🇲🇦' },
    { name: 'France', code: '+33', flag: '🇫🇷' },
    { name: 'Algérie', code: '+213', flag: '🇩🇿' },
    { name: 'Tunisie', code: '+216', flag: '🇹🇳' },
    { name: 'Espagne', code: '+34', flag: '🇪🇸' },
    { name: 'Allemagne', code: '+49', flag: '🇩🇪' },
    { name: 'Italie', code: '+39', flag: '🇮🇹' },
    { name: 'Royaume-Uni', code: '+44', flag: '🇬🇧' },
    { name: 'États-Unis', code: '+1', flag: '🇺🇸' },
    { name: 'Canada', code: '+1', flag: '🇨🇦' },
    { name: 'Pays-Bas', code: '+31', flag: '🇳🇱' },
    { name: 'Belgique', code: '+32', flag: '🇧🇪' },
    { name: 'Suisse', code: '+41', flag: '🇨🇭' },
    { name: 'Autriche', code: '+43', flag: '🇦🇹' },
    { name: 'Portugal', code: '+351', flag: '🇵🇹' },
    { name: 'Égypte', code: '+20', flag: '🇪🇬' },
    { name: 'Arabie Saoudite', code: '+966', flag: '🇸🇦' },
    { name: 'Émirats Arabes Unis', code: '+971', flag: '🇦🇪' },
    { name: 'Qatar', code: '+974', flag: '🇶🇦' },
    { name: 'Koweït', code: '+965', flag: '🇰🇼' }
  ];

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
      areaCode: ['', [Validators.required]],
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

    // Combine area code and phone number
    const formData = {
      ...this.registerForm.value,
      telephone: `${this.registerForm.value.areaCode}${this.registerForm.value.telephone}`
    };
    
    // Remove areaCode from the final data
    delete formData.areaCode;

    this.authService.registerClient(formData).subscribe({
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