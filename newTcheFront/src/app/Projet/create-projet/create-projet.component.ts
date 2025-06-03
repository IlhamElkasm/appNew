import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { ProjetService } from 'src/app/Service/projet.service';
import { CloudinaryService } from 'src/app/Service/cloudinary.service';

@Component({
  selector: 'app-create-projet',
  templateUrl: './create-projet.component.html',
  styleUrls: ['./create-projet.component.css']
})
export class CreateProjetComponent {
  project: any = {
    titre: '',
    description: '',
    imageUrl: ''
  };
  imageFile?: File;
  isUploading = false;
  
  constructor(
    private projetService: ProjetService,
    private cloudinaryService: CloudinaryService,
    private router: Router
  ) {}

  // ✅ أضف هذه الدالة
  onFileSelected(event: any): void {
    const file = event.target.files[0];
    if (file) {
      this.imageFile = file;
    }
  }

  addProject() {
    if (!this.imageFile) {
      alert('Veuillez sélectionner une image.');
      return;
    }
  
    this.isUploading = true;
    
    this.cloudinaryService.uploadImage(this.imageFile).subscribe(
      response => {
        this.project.imageUrl = response.secure_url;
        this.projetService.addProject(this.project).subscribe(
          () => {
            alert('Projet ajouté avec succès');
            this.router.navigate(['/Listprojets']);
            this.isUploading = false;
          },
          error => {
            alert('Erreur lors de l\'ajout du projet. Veuillez réessayer.');
            console.error(error);
            this.isUploading = false;
          }
        );
      },
      error => {
        alert('Erreur lors de l\'upload de l\'image. Veuillez réessayer.');
        console.error(error);
        this.isUploading = false;
      }
    );
  }
}
