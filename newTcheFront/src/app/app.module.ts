import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { CreateProjetComponent } from './Projet/create-projet/create-projet.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { HomeComponent } from './Home/home.component';
import { ProjectListComponent } from './Projet/project-list/project-list.component';
import { LoginComponent } from './auth/components/login/login.component';
import { PaiementComponent } from './paiement/paiement.component';
import { ModifierProjetComponent } from './Projet/modifier-projet/modifier-projet.component';
import { CreateFormationComponent } from './Formations/create-formation/create-formation.component';
import { ListFormationComponent } from './Formations/list-formation/list-formation.component';
import { ModifierFormationComponent } from './Formations/modifier-formation/modifier-formation.component';
import { RouterModule } from '@angular/router';
import { RegisterClientComponent } from './auth/components/register-client/register-client.component';
import { RegisterSecretaireComponent } from './auth/components/register-secretaire/register-secretaire.component';
import { AuthInterceptor } from './core/interceptors/auth.interceptor';
import { NavbarComponent } from './shared/navbar/navbar.component';
import { CommonModule } from '@angular/common';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ToastrModule } from 'ngx-toastr';
import { FormationDetailsComponent } from './Formations/formation-details/formation-details.component';
import { DashboardAdminComponent } from './Dashboard/dashboard-admin/dashboard-admin.component';
import { LocationComponent } from './Home/location/location.component';
import { PaymentSuccessComponent } from './paiement/payment-success/payment-success.component';
import { ReservationListComponent } from './Reservation/reservation-list/reservation-list.component';

import { MatCardModule } from '@angular/material/card';
import { MatGridListModule } from '@angular/material/grid-list';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { ProjectDetailsComponent } from './Projet/project-details/project-details.component';


@NgModule({
  declarations: [
    AppComponent,
    CreateProjetComponent,
    HomeComponent,
    ProjectListComponent,
    LoginComponent,
    PaiementComponent,
    ModifierProjetComponent,
    CreateFormationComponent,
    ListFormationComponent,
    ModifierFormationComponent,
    RegisterClientComponent,
    RegisterSecretaireComponent,
    NavbarComponent,
    FormationDetailsComponent,
    DashboardAdminComponent,
    LocationComponent,
    PaymentSuccessComponent,
    ReservationListComponent,
    ProjectDetailsComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ReactiveFormsModule, // Already imported
    HttpClientModule,
    RouterModule,
    FormsModule,
    CommonModule, // Add this import
    BrowserAnimationsModule, // obligatoire
    ToastrModule.forRoot({
      timeOut: 3000,
      positionClass: 'toast-bottom-right',
      preventDuplicates: true,
    }),
    MatCardModule,
    MatButtonModule,
    MatGridListModule,
    MatTableModule,
    MatButtonModule,
    MatIconModule,
    
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
