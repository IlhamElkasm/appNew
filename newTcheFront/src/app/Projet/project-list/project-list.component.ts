import { Component, OnInit } from '@angular/core';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';
import { Router } from '@angular/router';
import { Projet } from 'src/app/Model/Projet';
import { ProjetService } from 'src/app/Service/projet.service';


@Component({
  selector: 'app-project-list',
  templateUrl: './project-list.component.html',
  styleUrls: ['./project-list.component.css']
})
export class ProjectListComponent implements OnInit {
  projects: any[] = [];

  constructor(private projectService: ProjetService, private router: Router) {}

  ngOnInit() {
    this.loadProjects();
  }

  // 🔹 Charger la liste des projets
  loadProjects() {
    this.projectService.getProjects().subscribe(data => {
      this.projects = data;
    });
  }

  // 🔹 Supprimer un projet
  deleteProject(id: number) {
    if (confirm('Voulez-vous vraiment supprimer ce projet ?')) {
      this.projectService.deleteProject(id).subscribe(() => {
        this.loadProjects(); // Rafraîchir la liste après suppression
      });
    }
  }

  // 🔹 Aller vers la page d'édition
  editProject(id: number) {
    this.router.navigate(['/edit-project', id]); // Redirection vers l'édition
  }
}