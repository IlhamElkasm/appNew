// src/app/auth/models/registration.model.ts
export interface BaseRegistration {
  nom: string;
  email: string;
  password: string;
  roles?: string[];
}