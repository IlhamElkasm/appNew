import { Component } from '@angular/core';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {

  showLogin: boolean = true;

  toggleForm() {
    this.showLogin = !this.showLogin;
  }
}
