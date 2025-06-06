package org.example.reservationservice.reservation.controller;

import lombok.RequiredArgsConstructor;
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

    @PostMapping
    public ResponseEntity<Reservation> create(
            @RequestBody ReservationDTO dto,
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.substring(7);
        String username = jwtUtil.extractUsername(token);

        Long userId = userClient.getUserIdByUsername(username);
        dto.setUserId(userId);

        return ResponseEntity.ok(reservationService.createReservation(dto));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Reservation>> getByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(reservationService.getByUserId(userId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reservationService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}