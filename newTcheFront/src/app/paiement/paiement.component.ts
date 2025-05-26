import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { PaymentService } from 'src/app/Service/payment.service';
import { ReservationService } from 'src/app/Service/reservation.service';
import { Reservation } from 'src/app/Model/Reservation';
import { Payment, PaymentStatus } from 'src/app/Model/Payment';
import { AuthService } from '../auth/services/auth.service';

@Component({
  selector: 'app-paiement',
  templateUrl: './paiement.component.html',
  styleUrls: ['./paiement.component.css']
})
export class PaiementComponent implements OnInit {
  paymentForm: FormGroup;
  loading = false;
  reservation: Reservation | undefined;
  reservationId: number | undefined;
  
  cardTypes = [
    { name: "Visa", img: "https://i.imgur.com/2ISgYja.png" },
    { name: "Mastercard", img: "https://i.imgur.com/W1vtnOV.png" },
    { name: "American Express", img: "https://i.imgur.com/35tC99g.png" }
  ];

  constructor(
    private fb: FormBuilder,
    private paymentService: PaymentService,
    private reservationService: ReservationService,
    private toastr: ToastrService,
    private route: ActivatedRoute,
    public authService: AuthService,
    private router: Router
  ) {
    this.paymentForm = this.fb.group({
      cardNumber: ['', [Validators.required, Validators.pattern(/^\d{16}$/)]],
      expiryDate: ['', [Validators.required, Validators.pattern(/^(0[1-9]|1[0-2])\/\d{2}$/)]],
      cvv: ['', [Validators.required, Validators.pattern(/^\d{3,4}$/)]],
      cardholderName: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    // Get the reservation ID from query params or local storage
    this.route.queryParams.subscribe(params => {
      this.reservationId = params['reservationId'] || localStorage.getItem('currentReservationId');
      
      if (this.reservationId) {
        this.loadReservationDetails(Number(this.reservationId));
      } else {
        // Handle missing reservation ID - redirect to reservations or show error
        this.toastr.error('No reservation selected for payment', 'Error');
        this.router.navigate(['/reservations']);
      }
    });

     this.logUserInfo();
  }

logUserInfo() {
    const currentUser = this.authService.currentUserValue;
    console.log('🧠 Current User:', currentUser);
    console.log('🔐 User Roles:', currentUser?.roles);
    console.log('👑 Is Admin?', this.authService.isAdmin());
    console.log('👑 Is Client?', this.authService.isClient());
    console.log('👑 Is Secretaire?', this.authService.isSecretaire());
  }


  loadReservationDetails(id: number): void {
    this.reservationService.getReservationById(id).subscribe({
      next: (data) => {
        this.reservation = data;
      },
      error: (err) => {
        console.error('Error loading reservation:', err);
        this.toastr.error('Could not load reservation details', 'Error');
      }
    });
  }

  processPayment(): void {
    if (this.paymentForm.invalid) {
      this.toastr.error('Please fill all payment information correctly', 'Validation Error');
      return;
    }

    if (!this.reservation || !this.reservation.formation || !this.reservationId) {
      this.toastr.error('No reservation selected for payment', 'Error');
      return;
    }

    this.loading = true;
    
    // Use the formation price as payment amount
    const amount = this.reservation.formation.price;

    this.paymentService.processPayment(this.reservationId, amount).subscribe({
      next: (response) => {
        this.loading = false;
        this.toastr.success('Payment processed successfully', 'Success');
        
        // Clear the current reservation from storage
        localStorage.removeItem('currentReservationId');
        
        // Navigate to success page or reservations list
        this.router.navigate(['/payment-success']);
      },
      error: (error) => {
        this.loading = false;
        console.error('Payment error:', error);
        
        let errorMessage = 'Failed to process payment';
        if (error.message) {
          errorMessage = error.message;
        }
        
        this.toastr.error(errorMessage, 'Payment Failed');
      }
    });
  }

  // Helper method to check if a field is invalid after being touched
  isFieldInvalid(fieldName: string): boolean {
    const field = this.paymentForm.get(fieldName);
    return field ? (field.invalid && (field.dirty || field.touched)) : false;
  }

  // Format card number with spaces for display
  formatCardNumber(event: any): void {
    let value = event.target.value.replace(/\s/g, '');
    if (value.length > 16) {
      value = value.substr(0, 16);
    }
    // Format with a space every 4 digits
    value = value.replace(/(\d{4})(?=\d)/g, '$1 ');
    event.target.value = value;
  }

  // Format expiry date as MM/YY
  formatExpiryDate(event: any): void {
    let value = event.target.value.replace(/\D/g, '');
    if (value.length > 4) {
      value = value.substr(0, 4);
    }
    if (value.length > 2) {
      value = value.substr(0, 2) + '/' + value.substr(2);
    }
    event.target.value = value;
  }
}