package org.example.paymentservice.payment.service;

import lombok.RequiredArgsConstructor;
import org.example.paymentservice.payment.model.Payment;
import org.example.paymentservice.payment.model.PaymentStatus;
import org.example.paymentservice.payment.repository.PaymentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);

    private final PaymentRepository paymentRepository;

    public Payment simulatePayment(Long userId, Long reservationId, Double amount) {
        String transactionId = UUID.randomUUID().toString();

        Payment payment = Payment.builder()
                .userId(userId)
                .reservationId(reservationId)
                .amount(amount)
                .status(PaymentStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        paymentRepository.save(payment);

        try {
            // Symulacja opóźnienia
            Thread.sleep(500 + (long)(Math.random() * 1500));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            payment.setStatus(PaymentStatus.TIMEOUT);
            paymentRepository.save(payment);
            logger.error("Payment interrupted, transactionId: {}", transactionId);
            return payment;
        }

        Double chance = Math.random();
        if (chance < 0.8) {
            payment.setStatus(PaymentStatus.SUCCESS);
            logger.info("Payment SUCCESS, transactionId: {}", transactionId);
        } else if (chance < 0.95) {
            payment.setStatus(PaymentStatus.FAILED);
            logger.warn("Payment FAILED, transactionId: {}", transactionId);
        } else {
            payment.setStatus(PaymentStatus.TIMEOUT);
            logger.error("Payment TIMEOUT, transactionId: {}", transactionId);
        }

        paymentRepository.save(payment);
        return payment;
    }
}
