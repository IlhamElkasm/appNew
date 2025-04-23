package com.newtech.Repositories;

import com.newtech.Enum.NotificationType;
import com.newtech.Model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserIdAndIsReadFalse(Long userId);
    List<Notification> findByUserIdAndType(Long userId, NotificationType type);
}
