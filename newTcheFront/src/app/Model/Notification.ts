// src/app/Model/Notification.ts
export interface Notification {
  id?: number;
  message: string;
  isRead: boolean;
  createdAt: Date;
  userId?: number;
  reservationId?: number;
  clientName?: string;
  formationTitle?: string;
}