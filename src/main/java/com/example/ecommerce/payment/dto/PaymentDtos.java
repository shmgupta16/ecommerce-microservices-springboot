package com.example.ecommerce.payment.dto;

import com.example.ecommerce.payment.PaymentStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.Instant;

public class PaymentDtos {
    public record PaymentReceipt(Long paymentId, Long orderId, BigDecimal amount, String method,
                                 PaymentStatus status, String transactionReference, Instant paidAt) {}
    public record CheckoutSessionRequest(@NotNull Long orderId,
                                          @NotNull @DecimalMin("1.00") BigDecimal amount,
                                          @NotBlank String paymentMethod,
                                          String successUrl,
                                          String cancelUrl) {}
    public record CheckoutSessionResponse(String sessionId, String provider, String checkoutUrl,
                                          BigDecimal amount, String paymentMethod) {}
}
