package org.example.paymentservice.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.paymentservice.payment.model.PaymentStatus;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentDto {
    private Long userId;
    private Long reservationId;
    private Double amount;
    private PaymentStatus status;
    private LocalDateTime createdAt;
}