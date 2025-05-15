package com.newtech.Services;

import com.newtech.Enum.NotificationType;
import com.newtech.Model.Notification;
import com.newtech.Model.Reservation;
import com.newtech.Model.User;
import com.newtech.Repositories.NotificationRepository;
import com.newtech.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;

    public Notification createNotification(String message) {
        Notification notif = new Notification();
        notif.setMessage(message);
        notif.setCreatedAt(LocalDateTime.now());
        return notificationRepository.save(notif);
    }

    public List<Notification> getUnreadNotifications() {
        return notificationRepository.findByIsReadFalseOrderByCreatedAtDesc();
    }

    public void markAsRead(Long id) {
        Notification notif = notificationRepository.findById(id).orElseThrow();
        notif.setRead(true);
        notificationRepository.save(notif);
    }
}
