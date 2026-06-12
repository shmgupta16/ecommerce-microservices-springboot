package com.example.ecommerce.payment.dto;

import com.example.ecommerce.payment.PaymentStatus;
import java.math.BigDecimal;
import java.time.Instant;

public class PaymentDtos {
    public record PaymentReceipt(Long paymentId, Long orderId, BigDecimal amount, String method,
                                 PaymentStatus status, String transactionReference, Instant paidAt) {}
}
