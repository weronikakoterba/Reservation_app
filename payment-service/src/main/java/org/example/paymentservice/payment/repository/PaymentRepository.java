package org.example.paymentservice.payment.repository;

import org.example.paymentservice.payment.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
