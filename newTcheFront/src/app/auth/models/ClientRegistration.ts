import { BaseRegistration } from "./registration.model";

export interface ClientRegistration extends BaseRegistration {
    telephone: string;
    adresse: string;
  }