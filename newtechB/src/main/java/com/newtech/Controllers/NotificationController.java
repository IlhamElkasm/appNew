package com.newtech.Controllers;

import com.newtech.Model.Notification;
import com.newtech.Model.User;
import com.newtech.Repositories.NotificationRepository;
import com.newtech.Repositories.UserRepository;
import com.newtech.Services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin("*")

public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @GetMapping("/unread")
    public List<Notification> getUnread() {
        return notificationService.getUnreadNotifications();
    }

    @PostMapping("/mark-as-read/{id}")
    public void markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
    }
}
