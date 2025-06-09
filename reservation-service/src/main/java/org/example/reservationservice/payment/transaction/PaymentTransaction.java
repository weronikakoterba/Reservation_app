package org.example.reservationservice.payment.transaction;

import lombok.RequiredArgsConstructor;
import org.example.reservationservice.payment.dto.PaymentDTO;
import org.example.reservationservice.payment.dto.PaymentRequestDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class PaymentTransaction {

    private final RestTemplate restTemplate;

    public PaymentDTO processPayment(Long userId, Long reservationId, Double amount) {
        String url = "http://localhost:8082/payment/process";

        PaymentRequestDTO paymentRequest = new PaymentRequestDTO(userId, reservationId, amount);

        PaymentDTO paymentResponse = restTemplate.postForObject(url, paymentRequest, PaymentDTO.class);

        if (paymentResponse == null) {
            throw new RuntimeException("Payment service returned null response");
        }

        return paymentResponse;
    }
}