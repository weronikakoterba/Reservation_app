package org.example.reservationservice.reservation.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @Column(nullable = true)
    private Double price;

    private LocalDateTime startTime;

    @Enumerated(EnumType.STRING)
    private ServiceType serviceType;

    private String description;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

}
