package org.example.reservationservice.reservation;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.example.reservationservice.reservation.model.Reservation;
import org.example.reservationservice.reservation.model.ReservationStatus;
import org.example.reservationservice.reservation.model.ServiceType;
import org.example.reservationservice.reservation.repository.ReservationRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final ReservationRepository reservationRepository;
    @PostConstruct
    public void init() {
        LocalDate baseDate = LocalDate.now().plusDays(1);

        // Generuj sloty co 30 minut od 10:00 do 16:30
        for (ServiceType serviceType : ServiceType.values()) {
            for (int hour = 10; hour <= 16; hour++) {
                for (int minute : new int[]{0, 30}) {
                    LocalTime time = LocalTime.of(hour, minute);
                    if (hour == 16 && minute == 30) break; // opcjonalnie wyklucz 16:30 jeśli nie chcesz

                    Reservation reservation = Reservation.builder()
                            .userId(null)
                            .price(serviceType.getPrice())
                            .startTime(LocalDateTime.of(baseDate, time))
                            .serviceType(serviceType)
                            .description(serviceType.getDescription())
                            .status(ReservationStatus.AVAILABLE)
                            .build();

                    reservationRepository.save(reservation);
                }
            }
        }
    }
}
