package com.newtech.Services;


import com.newtech.Enum.NotificationType;
import com.newtech.Enum.UserRole;
import com.newtech.Model.Notification;
import com.newtech.Model.Payment;
import com.newtech.Model.Reservation;
//import com.newtech.Model.User;
import com.newtech.Model.User;
import com.newtech.Repositories.NotificationRepository;
import com.newtech.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Notification> getUnreadNotificationsByUserId(Long userId) {
        return notificationRepository.findByUserIdAndIsReadFalse(userId);
    }

    public Notification createNotification(User user, String message, NotificationType type, Long relatedId) {
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setMessage(message);
        notification.setType(type);
        notification.setRelatedId(relatedId);

        return notificationRepository.save(notification);
    }

    public void notifySecretariesAboutNewReservation(Reservation reservation) {
        List<User> secretaries = userRepository.findAll().stream()
                .filter(user -> user.getRole() == UserRole.SECRETAIRE)
                .collect(Collectors.toList());

        for (User secretary : secretaries) {
            createNotification(
                    secretary,
                    "New reservation from " + reservation.getClient().getNom() +
                            " " + reservation.getClient().getNom() +
                            " for formation: " + reservation.getFormation().getTitle(),
                    NotificationType.RESERVATION,
                    reservation.getId()
            );
        }
    }

    public void notifySecretariesAboutPayment(Payment payment) {
        List<User> secretaries = userRepository.findAll().stream()
                .filter(user -> user.getRole() == UserRole.SECRETAIRE)
                .collect(Collectors.toList());

        Reservation reservation = payment.getReservation();

        for (User secretary : secretaries) {
            createNotification(
                    secretary,
                    "Payment received from " + reservation.getClient().getNom() +
                            " " + reservation.getClient().getNom() +
                            " for formation: " + reservation.getFormation().getTitle() +
                            " (Amount: " + payment.getAmount() + ")",
                    NotificationType.PAYMENT,
                    payment.getId()
            );
        }
    }

    public Notification markNotificationAsRead(Long id) throws Exception {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new Exception("Notification not found with id: " + id));

        notification.setRead(true);
        return notificationRepository.save(notification);
    }
}

