package org.example.notificationservice.notification.service;

import org.example.notificationservice.notification.dto.NotificationDTO;
import org.springframework.stereotype.Service;

//@Service
//public class NotificationService {
//
//    public void sendNotification(NotificationDTO request) {
//        System.out.println(">>> Powiadomienie do: " + request.getRecipient());
//        System.out.println(">>> Wiadomość: " + request.getMessage());
//    }
//}


import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("zako-reservations@gmail.com"); // ten sam co w konfiguracji
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }
}
