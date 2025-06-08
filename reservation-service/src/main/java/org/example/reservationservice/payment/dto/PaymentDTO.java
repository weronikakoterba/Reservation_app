package org.example.reservationservice.payment.dto;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class PaymentDTO {
    private Long paymentId;
    private Long userId;
    private Long reservationId;
    private Double amount;
    private PaymentStatus status;
}

