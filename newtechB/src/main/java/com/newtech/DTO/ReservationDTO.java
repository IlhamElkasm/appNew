package com.newtech.DTO;

import com.newtech.Enum.ReservationStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReservationDTO {
    private Long id;
    private Long clientId;
    private Long formationId;
    private LocalDateTime reservedAt;
    private ReservationStatus status;
    private Long paymentId;
}
