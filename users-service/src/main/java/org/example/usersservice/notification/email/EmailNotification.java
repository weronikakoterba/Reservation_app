package org.example.usersservice.notification.email;

import lombok.RequiredArgsConstructor;
import org.example.usersservice.notification.dto.NotificationDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class EmailNotification {

    private final RestTemplate restTemplate;

    public void sendEmail(String recipientEmail, String message) {
        String url = "http://localhost:8083/notify";

        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setRecipient(recipientEmail);
        notificationDTO.setMessage(message);

        restTemplate.postForEntity(url, notificationDTO, Void.class);
    }
}