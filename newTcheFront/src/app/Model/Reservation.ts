// src/app/model/reservation.model.ts

import { ClientRegistration } from "../auth/models/ClientRegistration";
import { User } from "../auth/models/User";
import { Formation } from "./Formation";
import { ReservationStatus } from "./ReservationStatus";

export interface Reservation {
  id?: number;
  client?: ClientRegistration;
  formation: Formation;
  reservedAt: Date;
  status: ReservationStatus;
  payment?: any;
}

