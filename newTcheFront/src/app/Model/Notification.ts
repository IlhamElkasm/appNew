export interface Notification {
    id: number;
    message: string;
    createdAt: string;
    isRead: boolean;
    type: string;
    relatedId: number;
  }
  