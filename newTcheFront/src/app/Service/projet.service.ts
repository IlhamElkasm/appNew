import { Injectable } from '@angular/core';
import { Projet } from '../Model/Projet';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class ProjetService {
  private apiUrl = 'http://localhost:8081/api/projects';
  
  constructor(private http: HttpClient) {}

  getProjects(): Observable<Projet[]> {
    return this.http.get<Projet[]>(`${this.apiUrl}/all`);
  }

  getProjectById(id: number): Observable<Projet> {
    return this.http.get<Projet>(`${this.apiUrl}/admin/${id}`);
  }

  addProject(project: Projet): Observable<Projet> {
    return this.http.post<Projet>(`${this.apiUrl}/admin/create`, project);
  }

  updateProject(id: number, project: Projet): Observable<Projet> {
    return this.http.put<Projet>(`${this.apiUrl}/admin/${id}`, project);
  }

  deleteProject(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/admin/${id}`);
  }
}