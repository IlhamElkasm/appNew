package com.newtech.Services;



import com.newtech.DTO.NotificationDTO;
import com.newtech.Enum.UserRole;
import com.newtech.Model.Notification;
import com.newtech.Model.Reservation;
import com.newtech.Model.User;
import com.newtech.Repositories.NotificationRepository;
import com.newtech.Repositories.UserRepository;
import com.newtech.Mapper.NotificationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationMapper notificationMapper;

    public void createReservationNotificationForAdmin(Reservation reservation) {
        try {
            System.out.println("Creating notification for admins...");

            List<User> adminUsers = userRepository.findByRole(UserRole.ADMIN);
            System.out.println("Found " + adminUsers.size() + " admin(s).");

            String message = String.format(
                    "New reservation made by %s for formation: %s",
                    reservation.getClient() != null ? reservation.getClient().getNom() : "null",
                    reservation.getFormation() != null ? reservation.getFormation().getTitle() : "null"
            );

            for (User admin : adminUsers) {
                Notification notification = new Notification();
                notification.setUser(admin);
                notification.setReservation(reservation);
                notification.setMessage(message);
                notification.setCreatedAt(LocalDateTime.now());
                notification.setRead(false);
                notificationRepository.save(notification);

                System.out.println("Notification saved for admin: " + admin.getEmail());
            }
        } catch (Exception e) {
            System.err.println("Exception in createReservationNotificationForAdmin: " + e.getMessage());
            e.printStackTrace();
        }
    }



    public List<NotificationDTO> getNotificationsByUserId(Long userId) {
        List<Notification> notifications = notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
        return notificationMapper.toDtoList(notifications);
    }

    public List<NotificationDTO> getUnreadNotificationsByUserId(Long userId) {
        List<Notification> notifications = notificationRepository.findByUserIdAndIsReadFalseOrderByCreatedAtDesc(userId);
        return notificationMapper.toDtoList(notifications);
    }

    public long countUnreadNotificationsByUserId(Long userId) {
        return notificationRepository.countByUserIdAndIsReadFalse(userId);
    }

    @Transactional
    public void markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.setRead(true);
        notificationRepository.save(notification);
    }

    @Transactional
    public void markAllAsReadForUser(Long userId) {
        List<Notification> unreadNotifications = notificationRepository.findByUserIdAndIsReadFalse(userId);
        for (Notification notification : unreadNotifications) {
            notification.setRead(true);
        }
        notificationRepository.saveAll(unreadNotifications);
    }
}