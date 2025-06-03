import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Formation } from 'src/app/Model/Formation';
import { FormationService } from 'src/app/Service/formation.service';

@Component({
  selector: 'app-list-formation',
  templateUrl: './list-formation.component.html',
  styleUrls: ['./list-formation.component.css']
})
export class ListFormationComponent implements OnInit {
  formations: Formation[] = [];
  displayedColumns: string[] = ['image', 'title', 'description', 'price', 'actions'];
  isLoading = true;

  constructor(private formationService: FormationService,
    private router:Router
  ) { }

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

  viewFormation(id: number) {
    console.log('View formation', id);
  }

  editFormation(id: number) {
  this.router.navigate(['/edit-formation', id]);
  }

  deleteFormation(id: number) {
    if (confirm('Are you sure you want to delete this formation?')) {
      this.formationService.deleteFormation(id).subscribe(() => {
        this.formations = this.formations.filter(f => f.id !== id);
      });
    }
  }
}
