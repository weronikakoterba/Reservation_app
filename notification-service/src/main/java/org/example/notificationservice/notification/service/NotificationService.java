package org.example.notificationservice.notification.service;

import org.example.notificationservice.notification.dto.NotificationDTO;
import org.springframework.stereotype.Service;
import jakarta.mail.MessagingException;
import org.springframework.mail.javamail.JavaMailSender;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.MimeMessageHelper;

@Service
public class NotificationService {

    private final JavaMailSender mailSender;

    public NotificationService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendNotification(NotificationDTO request) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(request.getRecipient());
            helper.setSubject("Nowa wiadomość z aplikacji rezerwacyjnej");
            helper.setText(request.getMessage(), true); // HTML enabled

            mailSender.send(message);
            System.out.println("Wysłano e-mail do: " + request.getRecipient());

        } catch (MessagingException e) {
            e.printStackTrace();
            System.err.println("Błąd podczas wysyłania wiadomości: " + e.getMessage());
        }
    }
}
