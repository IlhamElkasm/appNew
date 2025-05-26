import { PaymentStatus } from "./PaymentStatus";

export interface Payment {
  id?: number;
  reservationId?: number;
  amount: number;
  paymentDate?: Date;
  status?: PaymentStatus;
  transactionId?: string;
}

export { PaymentStatus };
