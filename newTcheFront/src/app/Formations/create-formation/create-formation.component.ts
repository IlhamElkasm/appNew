import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { Formation } from 'src/app/Model/Formation';
import { CloudinaryService } from 'src/app/Service/cloudinary.service';
import { FormationService } from 'src/app/Service/formation.service';

@Component({
  selector: 'app-create-formation',
  templateUrl: './create-formation.component.html',
  styleUrls: ['./create-formation.component.css']
})
export class CreateFormationComponent {
  formation: Formation = new Formation();
  selectedFile: File | null = null;
  isUploading = false;
  errorMessage: string | null = null;

  constructor(
    private formationService: FormationService,
    private cloudinaryService: CloudinaryService,
    private router: Router
  ) {}

  onFileSelected(event: any): void {
    const file = event.target.files[0];
    if (file) {
      if (!file.type.match('image.*')) {
        this.errorMessage = 'Only image files are allowed';
        return;
      }
      if (file.size > 5 * 1024 * 1024) {
        this.errorMessage = 'File size should be less than 5MB';
        return;
      }
      this.selectedFile = file;
      this.errorMessage = null;
    }
  }

  onSubmit(): void {
    if (!this.validateForm()) {
      return;
    }

    if (this.selectedFile) {
      this.uploadImageAndCreate();
    } else {
      this.createFormation();
    }
  }

  private validateForm(): boolean {
    if (!this.formation.title.trim()) {
      this.errorMessage = 'Title is required';
      return false;
    }
    if (this.formation.price < 0) {
      this.errorMessage = 'Price cannot be negative';
      return false;
    }
    this.errorMessage = null;
    return true;
  }

  private uploadImageAndCreate(): void {
    this.isUploading = true;
    this.cloudinaryService.uploadImage(this.selectedFile!).subscribe({
      next: (response: any) => {
        this.formation.imageUrl = response.secure_url;
        this.createFormation();
      },
      error: (err) => {
        console.error('Upload error:', err);
        this.errorMessage = 'Failed to upload image';
        this.isUploading = false;
      }
    });
  }

  private createFormation(): void {
    const payload = {
      ...this.formation,
    };

    this.formationService.createFormation(payload as any).subscribe({
      next: () => {
        this.router.navigate(['/listformation']);
      },
      error: (err) => {
        console.error('Create error:', err);
        this.errorMessage = 'Failed to create formation';
        this.isUploading = false;
      }
    });
    
  }

  private formatDate(date: any): string {
    const d = (date instanceof Date) ? date : new Date(date);
    return d.toISOString().split('T')[0]; // 'yyyy-MM-dd'
  }
  

  onCancel(): void {
    this.router.navigate(['/listformation']);
  }
}
