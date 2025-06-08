package org.example.reservationservice.reservation.model;

public enum ReservationStatus {
    AVAILABLE,         // wolny termin
    PENDING,   // oczekuje na płatność
    PAID,              // zapłacona, rezerwacja potwierdzona
    CANCELLED,
    RESERVED
}
