import { HttpClient } from '@angular/common/http';
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { Formation } from 'src/app/Model/Formation';
import { CloudinaryService } from 'src/app/Service/cloudinary.service';
import { FormationService } from 'src/app/Service/formation.service';
@Component({
  selector: 'app-modifier-formation',
  templateUrl: './modifier-formation.component.html',
  styleUrls: ['./modifier-formation.component.css']
})
export class ModifierFormationComponent implements OnInit {

  @Input() formation: Formation = {
    title: '',
    description: '',
    price: 0,
    imageUrl: ''
  };

  @Output() formSubmitted = new EventEmitter<Formation>();
  @Output() cancel = new EventEmitter<void>();

  selectedFile: File | null = null;
  isUploading = false;
  formationId!: number;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private formationService: FormationService,
    private cloudinaryService: CloudinaryService
  ) {}

  ngOnInit(): void {
    this.formationId = +this.route.snapshot.params['id'];
    this.loadFormation();
  }

  loadFormation() {
    this.formationService.getFormation(this.formationId).subscribe({
      next: (formation) => this.formation = { ...formation },
      error: (err) => console.error('Error loading formation:', err)
    });
  }

  onFileSelected(event: any) {
    this.selectedFile = event.target.files[0] as File;
  }

  onCancel() {
    this.cancel.emit();
    this.router.navigate(['/listformation']);
  }

  onSubmit() {
    if (this.selectedFile) {
      this.isUploading = true;
      this.cloudinaryService.uploadImage(this.selectedFile).subscribe({
        next: (response: any) => {
          this.formation.imageUrl = response.secure_url;
          this.updateFormation();
        },
        error: (err) => {
          console.error('Image upload failed:', err);
          this.isUploading = false;
        }
      });
    } else {
      this.updateFormation();
    }
  }

  private updateFormation() {
    this.formationService.updateFormation(this.formationId, this.formation).subscribe({
      next: () => this.router.navigate(['/listformation']),
      error: (err) => {
        console.error('Update failed:', err);
        this.isUploading = false;
      }
    });
  }
}
