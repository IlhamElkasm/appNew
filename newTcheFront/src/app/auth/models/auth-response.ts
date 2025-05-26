// src/app/auth/models/auth-response.model.ts
export interface AuthResponse {
    accessToken: string;
    tokenType: string;
    roles: string[];
    nom: string;  // Assurez-vous que ce champ existe et correspond à celui du backend
}