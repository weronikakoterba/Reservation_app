package org.example.notificationservice.service;

import org.example.notificationservice.dto.NotificationDTO;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    public void sendNotification(NotificationDTO request) {
        System.out.println(">>> Powiadomienie do: " + request.getRecipient());
        System.out.println(">>> Wiadomość: " + request.getMessage());
    }
}
