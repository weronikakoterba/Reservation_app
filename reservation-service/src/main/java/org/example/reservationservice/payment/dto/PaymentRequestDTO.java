package org.example.reservationservice.payment.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PaymentRequestDTO {
    private Long userId;
    private Long reservationId;
    private Double amount;

    public PaymentRequestDTO(Long userId, Long reservationId, Double amount) {
        this.userId = userId;
        this.reservationId = reservationId;
        this.amount = amount;
    }
}
