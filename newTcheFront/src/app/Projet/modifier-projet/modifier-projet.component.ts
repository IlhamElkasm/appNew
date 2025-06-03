import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { Projet } from 'src/app/Model/Projet';
import { CloudinaryService } from 'src/app/Service/cloudinary.service';
import { ProjetService } from 'src/app/Service/projet.service';

@Component({
  selector: 'app-modifier-projet',
  templateUrl: './modifier-projet.component.html',
  styleUrls: ['./modifier-projet.component.css']
})
export class ModifierProjetComponent implements OnInit {
  project: Projet = new Projet();
  selectedFile: File | null = null;
  

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private projetService: ProjetService,
    private cloudinaryService: CloudinaryService  // <-- Ajouté ici

  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.projetService.getProjectById(+id).subscribe(data => {
        this.project = data;
      }, error => {
        console.error('Erreur lors du chargement du projet:', error);
      });
    }
  }

  // 🔹 Gérer le changement de fichier pour l'image
  onFileSelected(event: any): void {
    this.selectedFile = event.target.files[0];
  }

  // 🔹 Télécharger l'image vers Cloudinary
  uploadImage(): Observable<any> {
    if (this.selectedFile) {
    return this.cloudinaryService.uploadImage(this.selectedFile);
    }
    return new Observable();  // Si aucun fichier n'est sélectionné
  }
 updateProject(): void {
  if (!this.project.id) {
    alert('Projet introuvable');
    return;
  }

  // Si un fichier est sélectionné, upload d'abord l'image
  if (this.selectedFile) {
    this.uploadImage().subscribe(
      imageResponse => {
        if (imageResponse?.secure_url) {
          this.project.imageUrl = imageResponse.secure_url;
        }
        this.saveProject(); // Enregistre après upload
      },
      error => {
        console.error('Erreur lors du téléchargement de l\'image:', error);
        alert("Échec du téléchargement de l'image.");
      }
    );
  } else {
    this.saveProject(); // Sinon enregistre directement
  }
}

private saveProject(): void {
  this.projetService.updateProject(this.project.id!, this.project).subscribe(() => {
    alert('Projet mis à jour avec succès');
    this.router.navigate(['/Listprojets']);
  }, error => {
    console.error('Erreur lors de la mise à jour:', error);
    alert('Erreur lors de la mise à jour du projet');
  });
}
}