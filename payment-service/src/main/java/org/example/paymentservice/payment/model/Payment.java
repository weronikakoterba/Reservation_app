package org.example.paymentservice.payment.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private Long reservationId;
    private Double amount;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;


    private LocalDateTime createdAt;



}
