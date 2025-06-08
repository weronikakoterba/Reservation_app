package org.example.reservationservice.notification.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationDTO {
    private String recipient;
    private String message;
}