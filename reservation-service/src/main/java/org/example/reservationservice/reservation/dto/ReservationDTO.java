package org.example.reservationservice.reservation.dto;

import lombok.*;
import org.example.reservationservice.reservation.model.ServiceType;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationDTO {
    private Long userId;
    private LocalDateTime startTime;
    private ServiceType serviceType;
    private String description;
}
