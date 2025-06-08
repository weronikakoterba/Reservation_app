package org.example.reservationservice.reservation.service;

import lombok.RequiredArgsConstructor;
import org.example.reservationservice.reservation.dto.ReservationDTO;
import org.example.reservationservice.reservation.model.Reservation;
import org.example.reservationservice.reservation.model.ReservationStatus;
import org.example.reservationservice.reservation.repository.ReservationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;


    public Reservation createSlot(ReservationDTO dto) {
        Reservation reservation = Reservation.builder()
                .userId(dto.getUserId())
                .price(dto.getPrice())
                .startTime(dto.getStartTime())
                .serviceType(dto.getServiceType())
                .description(dto.getDescription())
                .status(ReservationStatus.AVAILABLE)
                .build();
        return reservationRepository.save(reservation);
    }

    public Reservation bookReservation(Long reservationId, Long userId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono rezerwacji"));

        if (reservation.getStatus() != ReservationStatus.AVAILABLE) {
            throw new IllegalStateException("Termin już zajęty");
        }

        reservation.setUserId(userId);
        reservation.setStatus(ReservationStatus.RESERVED);
        return reservationRepository.save(reservation);
    }


    public Reservation reservePending(Long reservationId, Long userId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        if (reservation.getStatus() != ReservationStatus.AVAILABLE) {
            throw new IllegalStateException("Reservation is not available");
        }

        reservation.setUserId(userId);
        reservation.setStatus(ReservationStatus.PENDING);

        return reservationRepository.save(reservation);
    }

    public Reservation confirmReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        if (reservation.getStatus() != ReservationStatus.PENDING) {
            throw new IllegalStateException("Reservation is not pending");
        }

        reservation.setStatus(ReservationStatus.RESERVED);
        return reservationRepository.save(reservation);
    }

    public Reservation releaseReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        reservation.setUserId(null);
        reservation.setStatus(ReservationStatus.AVAILABLE);
        return reservationRepository.save(reservation);
    }



    public Reservation cancelReservation(Long reservationId, Long userId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono rezerwacji"));

        if (reservation.getStatus() == ReservationStatus.AVAILABLE) {
            throw new IllegalStateException("Termin wolny");
        }

        reservation.setUserId(null);
        reservation.setStatus(ReservationStatus.AVAILABLE);
        return reservationRepository.save(reservation);
    }

    public List<Reservation> getAvailableSlots() {
        return reservationRepository.findByStatus(ReservationStatus.AVAILABLE);
    }

    public List<Reservation> getByUserId(Long userId) {
        return reservationRepository.findByUserId(userId);
    }

    public Reservation findById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rezerwacja nie istnieje"));
    }

    public Reservation save(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }
}
