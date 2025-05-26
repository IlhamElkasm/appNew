package com.newtech.Mapper;

import com.newtech.DTO.NotificationDTO;
import com.newtech.Model.Notification;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class NotificationMapper {

    public NotificationDTO toDto(Notification notification) {
        if (notification == null) {
            return null;
        }

        NotificationDTO dto = new NotificationDTO();
        dto.setId(notification.getId());
        dto.setMessage(notification.getMessage());
        dto.setRead(notification.isRead());
        dto.setCreatedAt(notification.getCreatedAt());
        dto.setUserId(notification.getUser() != null ? notification.getUser().getId() : null);
        dto.setReservationId(notification.getReservation() != null ? notification.getReservation().getId() : null);

        if (notification.getReservation() != null) {
            if (notification.getReservation().getClient() != null) {
                dto.setClientName(notification.getReservation().getClient().getNom());
            }
            if (notification.getReservation().getFormation() != null) {
                dto.setFormationTitle(notification.getReservation().getFormation().getTitle());
            }
        }

        return dto;
    }

    public List<NotificationDTO> toDtoList(List<Notification> notifications) {
        return notifications.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}