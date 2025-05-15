import { Component, Input, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Projet } from '../Model/Projet';
import { ProjetService } from '../Service/projet.service';
import { FormationService } from '../Service/formation.service';
import { Formation } from '../Model/Formation';
import { AuthService } from '../auth/services/auth.service';
import { ToastrService } from 'ngx-toastr';
import { ReservationService } from '../Service/reservation.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  projets: Projet[] = [];
  formations: Formation[] = [];
  loading = false;

  newClientCount: number = 0;
  formationCount: number = 0;

  constructor(
    private projetService: ProjetService, 
    private formationService: FormationService,
    public authService: AuthService,
    private toastr: ToastrService,
    private reservationService: ReservationService,
    public router: Router
  ) {}

  ngOnInit() : void {

     this.formationService.getFormationCount()
      .subscribe((count) => {
        this.formationCount = count;
      });
    this.loadProjets();
    this.getFormations();
    this.logUserInfo();

    this.loadNewClientCount();
  }





  logUserInfo() {
    const currentUser = this.authService.currentUserValue;
    console.log('🧠 Current User:', currentUser);
    console.log('🔐 User Roles:', currentUser?.roles);
    console.log('👑 Is Admin?', this.authService.isAdmin());
    console.log('👑 Is Client?', this.authService.isClient());
    console.log('👑 Is Secretaire?', this.authService.isSecretaire());
  }

  // viewDetails(id?: number): void {
  //   if (id === undefined) {
  //     console.warn('Formation ID is undefined. Skipping navigation.');
  //     return;
  //   }
  //   this.router.navigate(['/formation-details', id]);
  // }
  

  loadNewClientCount() {
    this.authService.getNewClientCount().subscribe(count => {
      this.newClientCount = count;
    });
  }


  viewDetails(id: number): void {
  if (!this.authService.isLoggedIn) {
    // 🚫 User is not logged in
    alert('Vous devez être connecté pour voir ou réserver des formations, Non Connecté');
    this.router.navigate(['/login']);
    return;
  }

  if (!this.authService.isClient()) {
    // ⚠️ User is logged in but not a client
    this.toastr.warning('Seuls les clients peuvent réserver des formations. Veuillez utiliser un compte client Compte client Requis');
    this.router.navigate(['/signup']);
    return;
  }

  // ✅ User is logged in and is a client
  this.router.navigate(['/formation-details', id]);
}

  


  loadProjets() {
    this.projetService.getProjects().subscribe({
      next: (projets) => {
        this.projets = projets;
      },
      error: (error) => {
        console.error('Erreur lors du chargement des projets', error);
      }
    });
  }

  getFormations(): void {
    this.formationService.getAllFormations().subscribe({
      next: (data: Formation[]) => {
        this.formations = data;
      },
      error: (error) => {
        console.error('Error loading formations:', error);
      }
    });
  }

}
