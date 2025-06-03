import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Projet } from 'src/app/Model/Projet';
import { ProjetService } from 'src/app/Service/projet.service';


@Component({
  selector: 'app-project-details',
  templateUrl: './project-details.component.html',
  styleUrls: ['./project-details.component.css']
})
export class ProjectDetailsComponent implements OnInit {
  projectId!: number;
  project!: Projet;

  constructor(
    private route: ActivatedRoute,
    private projetService: ProjetService
  ) {}

  ngOnInit(): void {
    this.projectId = Number(this.route.snapshot.paramMap.get('id'));
    this.projetService.getProjectById(this.projectId).subscribe({
      next: (data) => this.project = data,
      error: (err) => console.error('Error fetching project:', err)
    });
  }
}
