package org.example.reservationservice.reservation.controller;

import lombok.RequiredArgsConstructor;
import org.example.reservationservice.payment.dto.PaymentDTO;
import org.example.reservationservice.payment.transaction.PaymentTransaction;
import org.example.reservationservice.reservation.dto.ReservationDTO;
import org.example.reservationservice.reservation.model.Reservation;
import org.example.reservationservice.reservation.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.example.reservationservice.reservation.security.JwtUtil;
import org.example.reservationservice.user.client.UserClient;

import java.util.List;

@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;
    private final JwtUtil jwtUtil;
    private final UserClient userClient;
    private final PaymentTransaction paymentTransaction;

    // Admin/Usługodawca tworzy wolny termin
    @PostMapping
    public ResponseEntity<Reservation> create(@RequestBody ReservationDTO dto) {
        return ResponseEntity.ok(reservationService.createSlot(dto));
    }

    // Użytkownik widzi dostępne sloty
    @GetMapping("/available")
    public ResponseEntity<List<Reservation>> getAvailable() {
        return ResponseEntity.ok(reservationService.getAvailableSlots());
    }

    // Użytkownik rezerwuje istniejący slot
    @PutMapping("/{id}/book")
    public ResponseEntity<Reservation> book(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.substring(7);
        String username = jwtUtil.extractUsername(token);
        Long userId = userClient.getUserIdByUsername(username);

        return ResponseEntity.ok(reservationService.bookReservation(id, userId));
    }

    @PutMapping("/{id}/bookWithPayment")
    public ResponseEntity<?> bookWithPayment(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.substring(7);
        String username = jwtUtil.extractUsername(token);
        Long userId = userClient.getUserIdByUsername(username);

        // 1. Rezerwacja slota – ale tylko zmiana statusu na PENDING_PAYMENT
        Reservation pendingReservation = reservationService.reservePending(id, userId);

        // 2. Symulacja płatności
        double amount = pendingReservation.getPrice();
        PaymentDTO payment = paymentTransaction.processPayment(userId, id, amount);

        // 3. Sprawdzenie wyniku płatności
        switch (payment.getStatus()) {
            case SUCCESS:
                Reservation confirmed = reservationService.confirmReservation(id);
                return ResponseEntity.ok(confirmed);

            case FAILED:
            case TIMEOUT:
                reservationService.releaseReservation(id); // zmiana statusu z powrotem na AVAILABLE
                return ResponseEntity.badRequest()
                        .body("Płatność nie powiodła się. Rezerwacja została anulowana.");

            default:
                return ResponseEntity.status(500)
                        .body("Wystąpił nieznany błąd podczas płatności.");
        }
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<Reservation> cancel(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.substring(7);
        String username = jwtUtil.extractUsername(token);
        Long userId = userClient.getUserIdByUsername(username);

        return ResponseEntity.ok(reservationService.cancelReservation(id, userId));
    }


    // Użytkownik widzi swoje rezerwacje
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Reservation>> getByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(reservationService.getByUserId(userId));
    }

    // Usunięcie rezerwacji (np. przez admina)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reservationService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}