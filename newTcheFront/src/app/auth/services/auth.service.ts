import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { User } from '../models/User';
import { AuthResponse } from '../models/auth-response';
import { UserRole } from '../models/user.model';
import { ClientRegistration } from '../models/ClientRegistration';
import { SecretaireRegistration } from '../models/SecretaireRegistration';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8081/api/auth';

  private currentUserSubject = new BehaviorSubject<User | null>(null);
  public currentUser$ = this.currentUserSubject.asObservable();
 
  constructor(private http: HttpClient) {
    this.loadUserFromLocalStorage();
  }

  private loadUserFromLocalStorage() {
    const token = localStorage.getItem('token');
    const userJson = localStorage.getItem('user');
    if (token && userJson) {
      this.currentUserSubject.next(JSON.parse(userJson));
    }
  }
  
  // Version modifiée pour accepter un objet formulaire ou des paramètres séparés
  login(emailOrFormValue: string | { email: string, password: string }, password?: string): Observable<AuthResponse> {
    let email: string;
    let pwd: string;
    
    // Vérifier si le premier paramètre est un objet ou une chaîne
    if (typeof emailOrFormValue === 'object') {
      email = emailOrFormValue.email;
      pwd = emailOrFormValue.password;
    } else {
      email = emailOrFormValue;
      pwd = password!; // L'opérateur ! indique à TypeScript que password n'est pas undefined
    }
    
    return this.http.post<AuthResponse>(`${this.apiUrl}/login`, { email, password: pwd }).pipe(
      tap(response => {
        localStorage.setItem('token', response.accessToken);
 
        const user: User = {
          email: email,
          nom: '',  // À compléter si disponible dans la réponse
          roles: response.roles.map(role => role as UserRole)
        };
 
        localStorage.setItem('user', JSON.stringify(user));
        this.currentUserSubject.next(user);
      })
    );
  }
 
  registerClient(client: ClientRegistration): Observable<User> {
    return this.http.post<User>(`${this.apiUrl}/register/client`, client);
  }
  
  registerSecretaire(secretaire: SecretaireRegistration): Observable<User> {
    return this.http.post<User>(`${this.apiUrl}/register/secretaire`, secretaire);
  }
  
  logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    this.currentUserSubject.next(null);
  }

  // src/app/auth/services/auth.service.ts
    getAllClients() {
      return this.http.get<ClientRegistration[]>(`${this.apiUrl}/clients`); // ou l'URL complète si nécessaire
    }


    getNewClientCount(): Observable<number> {
      return this.http.get<number>('http://localhost:8081/api/auth/clients/new/count');
    }
    
  
  get currentUserValue(): User | null {
    return this.currentUserSubject.value;
  }
  
  get isLoggedIn(): boolean {
    return !!this.currentUserValue;
  }
  
  // Méthodes hasRole et isAdmin dans auth.service.ts
hasRole(role: UserRole): boolean {
  const user = this.currentUserValue;
  if (!user || !user.roles) return false;
  return user.roles.includes(role);
}

isAdmin(): boolean {
  return this.hasRole(UserRole.ADMIN);
}

isSecretaire(): boolean {
  return this.hasRole(UserRole.SECRETAIRE);
}

isClient(): boolean {
  return this.hasRole(UserRole.CLIENT);
}
}