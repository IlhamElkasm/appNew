import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
// Components
import { CreateProjetComponent } from './Projet/create-projet/create-projet.component';
import { HomeComponent } from './Home/home.component';
import { ProjectListComponent } from './Projet/project-list/project-list.component';
import { PaiementComponent } from './paiement/paiement.component';
import { CreateFormationComponent } from './Formations/create-formation/create-formation.component';
import { ListFormationComponent } from './Formations/list-formation/list-formation.component';
import { ModifierFormationComponent } from './Formations/modifier-formation/modifier-formation.component';
import { RegisterClientComponent } from './auth/components/register-client/register-client.component';
import { RegisterSecretaireComponent } from './auth/components/register-secretaire/register-secretaire.component';
import { UserRole } from './auth/models/user.model';
import { AuthGuard } from './core/guards/auth.guard';
import { LoginComponent } from './auth/components/login/login.component';
import { FormationDetailsComponent } from './Formations/formation-details/formation-details.component';
import { DashboardAdminComponent } from './Dashboard/dashboard-admin/dashboard-admin.component';
import { RedirectResolver } from './core/guards/redirect-resolver.guard';
import { LocationComponent } from './Home/location/location.component';
import { PaymentSuccessComponent } from './paiement/payment-success/payment-success.component';
import { ReservationListComponent } from './Reservation/reservation-list/reservation-list.component';

const routes: Routes = [
  // Route par défaut qui utilise un resolver pour rediriger en fonction du rôle
  {
    path: '',
    canActivate: [AuthGuard],
    component: HomeComponent,
  },
  { path: 'home', component: HomeComponent },
 
  {
    path: 'dashboardAdmin',
    component: DashboardAdminComponent,
    canActivate: [AuthGuard],
    data: { roles: [UserRole.ADMIN, UserRole.SECRETAIRE] }
  },
  // 🔹 تسجيل الدخول  
  { path: 'login', component: LoginComponent, data: { hideHeaderFooter: true } },
  // 🔹 تسجيل المستخدمين
  { path: 'register-client', component: RegisterClientComponent },
  {
    path: 'register-secretaire',
    component: RegisterSecretaireComponent,
    canActivate: [AuthGuard],
    data: { roles: [UserRole.ADMIN] }
  },
  {
    path: 'ListReservation',
    component: ReservationListComponent,
    canActivate: [AuthGuard],
    data: { roles: [UserRole.ADMIN] }
  },
  {
    path: 'formation-details/:id',
    component: FormationDetailsComponent,
    canActivate: [AuthGuard],
    data: { roles: [UserRole.CLIENT] }
  },
 
  // 🔹 إدارة المشاريع
  { 
    path: 'Listprojets', 
    component: ProjectListComponent, 
    canActivate: [AuthGuard] 
  },
  {
    path: 'projet',
    component: CreateProjetComponent,
    canActivate: [AuthGuard],
    data: { roles: [UserRole.ADMIN, UserRole.SECRETAIRE] }
  },
 
  // 🔹 إدارة التكوينات (Formations)
  {
    path: 'formation',
    component: CreateFormationComponent,
    canActivate: [AuthGuard],
    data: { roles: [UserRole.ADMIN, UserRole.SECRETAIRE] }
  },

  {
    path: 'location',
    component: LocationComponent,
    canActivate: [AuthGuard],
  },
  { 
    path: 'listformation', 
    component: ListFormationComponent,
    canActivate: [AuthGuard] 
  },
  {
    path: 'edit-formation/:id',
    component: ModifierFormationComponent,
    canActivate: [AuthGuard],
    data: { roles: [UserRole.ADMIN, UserRole.SECRETAIRE] }
  },
  // 🔹 الدفع ورفع الملفات
  { 
    path: 'paiement', 
    component: PaiementComponent, 
    canActivate: [AuthGuard] ,
    data: { roles: [UserRole.CLIENT] }
  },

  { 
    path: 'payment-success', 
    component: PaymentSuccessComponent, 
    canActivate: [AuthGuard] ,
    data: { roles: [UserRole.CLIENT] }
  },
  // 🔹 إدارة الأخطاء
  { path: '**', redirectTo: '/home' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {
    scrollPositionRestoration: 'enabled',
    anchorScrolling: 'enabled'
  })],
  exports: [RouterModule]
})
export class AppRoutingModule { }