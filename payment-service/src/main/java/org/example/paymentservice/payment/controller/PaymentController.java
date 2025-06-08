package org.example.paymentservice.payment.controller;

import lombok.RequiredArgsConstructor;
import org.example.paymentservice.payment.dto.PaymentDto;
import org.example.paymentservice.payment.model.Payment;
import org.example.paymentservice.payment.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping("/health")
    public String healthCheck() {
        return "Payment service is running";
    }

    @PostMapping("/process")
    public ResponseEntity<Payment> processPayment(@RequestBody PaymentDto request) {
        Payment paymentResult = paymentService.simulatePayment(request.getUserId(), request.getReservationId(), request.getAmount());
        return ResponseEntity.ok(paymentResult);
    }
}



