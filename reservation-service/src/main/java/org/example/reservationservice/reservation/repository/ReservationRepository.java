package org.example.reservationservice.reservation.repository;

import org.example.reservationservice.reservation.model.Reservation;
import org.example.reservationservice.reservation.model.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByUserId(Long userId);
    List<Reservation> findByStatus(ReservationStatus status);
}
