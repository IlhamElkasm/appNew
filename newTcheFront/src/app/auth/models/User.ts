import { UserRole } from "./user.model";


export interface User {
  id?: number;
  nom: string;
  email: string;
  roles?: UserRole[];
  active?: boolean;
}