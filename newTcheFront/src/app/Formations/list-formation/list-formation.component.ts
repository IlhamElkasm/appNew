import { Component, OnInit } from '@angular/core';
import { Formation } from 'src/app/Model/Formation';
import { FormationService } from 'src/app/Service/formation.service';

@Component({
  selector: 'app-list-formation',
  templateUrl: './list-formation.component.html',
  styleUrls: ['./list-formation.component.css']
})
export class ListFormationComponent implements OnInit {
  formations: Formation[] = [];
  isLoading = true;

  constructor(private formationService: FormationService) { }

  ngOnInit(): void {
    this.loadFormations();
  }

  loadFormations() {
    this.isLoading = true;
    this.formationService.getAllFormations().subscribe(
      formations => {
        this.formations = formations;
        this.isLoading = false;
      },
      error => {
        console.error('Error loading formations:', error);
        this.isLoading = false;
      }
    );
  }

  deleteFormation(id: number) {
    if (confirm('Are you sure you want to delete this formation?')) {
      this.formationService.deleteFormation(id).subscribe(
        () => {
          this.formations = this.formations.filter(f => f.id !== id);
        },
        error => {
          console.error('Error deleting formation:', error);
        }
      );
    }
  }
}