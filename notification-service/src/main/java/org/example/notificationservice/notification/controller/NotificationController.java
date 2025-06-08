package org.example.notificationservice.notification.controller;


import org.example.notificationservice.notification.dto.NotificationDTO;
import org.example.notificationservice.notification.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notify")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping
    public ResponseEntity<String> send(@RequestBody NotificationDTO request) {
        notificationService.sendNotification(request);
        return ResponseEntity.ok("Powiadomienie zostało wysłane (symulacja).");
    }
}
