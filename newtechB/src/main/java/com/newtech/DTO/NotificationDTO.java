package com.newtech.DTO;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class NotificationDTO {
    private Long id;
    private String message;
    private boolean isRead;
    private LocalDateTime createdAt;
    private Long userId;
    private Long reservationId;
    private String clientName;
    private String formationTitle;
}