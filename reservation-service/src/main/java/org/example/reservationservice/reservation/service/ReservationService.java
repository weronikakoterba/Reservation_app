package org.example.reservationservice.reservation.service;

import lombok.RequiredArgsConstructor;
import org.example.reservationservice.reservation.dto.ReservationDTO;
import org.example.reservationservice.reservation.model.Reservation;
import org.example.reservationservice.reservation.repository.ReservationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;

    public Reservation createReservation(ReservationDTO dto) {
        Reservation reservation = Reservation.builder()
                .userId(dto.getUserId())
                .startTime(dto.getStartTime())
                .serviceType(dto.getServiceType())
                .description(dto.getDescription())
                .build();
        return reservationRepository.save(reservation);
    }

    public List<Reservation> getByUserId(Long userId) {
        return reservationRepository.findByUserId(userId);
    }

    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }
}
