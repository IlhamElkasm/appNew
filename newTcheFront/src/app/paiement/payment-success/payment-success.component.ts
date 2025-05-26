import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-payment-success',
  templateUrl: './payment-success.component.html',
  styleUrls: ['./payment-success.component.css']
})
export class PaymentSuccessComponent implements OnInit {
  countdown: number = 5;
  countdownInterval: any;

  constructor(private router: Router) { }

  ngOnInit(): void {
    // Start countdown for automatic redirect
    this.countdownInterval = setInterval(() => {
      this.countdown--;
      if (this.countdown <= 0) {
        clearInterval(this.countdownInterval);
        this.router.navigate(['/reservations']);
      }
    }, 1000);
  }

  ngOnDestroy(): void {
    // Clear interval when component is destroyed
    if (this.countdownInterval) {
      clearInterval(this.countdownInterval);
    }
  }

  navigateToReservations(): void {
    clearInterval(this.countdownInterval);
    this.router.navigate(['/reservations']);
  }

  navigateToFormations(): void {
    clearInterval(this.countdownInterval);
    this.router.navigate(['/formations']);
  }
}