//package com.newtech.DTO;
//
//import com.newtech.Enum.ReservationStatus;
//import lombok.Data;
//
//import java.time.LocalDateTime;
//
//@Data
//public class ReservationDTO {
//    private Long id;
//    private Long clientId;
//    private Long formationId;
//    private LocalDateTime reservedAt;
//    private ReservationStatus status;
//    private Long paymentId;
//}

package com.newtech.DTO;

import com.newtech.Enum.ReservationStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReservationDTO {
    private Long id;
    private Long clientId;
    private String clientName; // Maps to client.nom
    private String clientEmail; // Maps to client.email
    private Long formationId;
    private String formationTitle; // Correction: formationTitle au lieu de formationtitle
    private String formationDescription; // Maps to formation.description
    private LocalDateTime reservedAt;
    private ReservationStatus status;
    private Long paymentId;
}
