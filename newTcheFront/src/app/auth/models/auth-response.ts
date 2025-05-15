// src/app/auth/models/auth-response.model.ts
export interface AuthResponse {
    accessToken: string;
    tokenType: string;
    roles: string[];
}