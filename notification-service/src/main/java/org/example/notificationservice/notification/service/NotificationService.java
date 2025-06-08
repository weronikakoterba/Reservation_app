package org.example.notificationservice.notification.service;

import org.example.notificationservice.notification.dto.NotificationDTO;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    public void sendNotification(NotificationDTO request) {
        System.out.println(">>> Powiadomienie do: " + request.getRecipient());
        System.out.println(">>> Wiadomość: " + request.getMessage());
    }
}
