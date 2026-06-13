package com.example.ecommerce.payment;

import com.example.ecommerce.common.events.DomainEventPublisher;
import com.example.ecommerce.payment.dto.PaymentDtos.CheckoutSessionRequest;
import com.example.ecommerce.payment.dto.PaymentDtos.CheckoutSessionResponse;
import com.example.ecommerce.payment.dto.PaymentDtos.PaymentReceipt;
import java.math.BigDecimal;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final DomainEventPublisher eventPublisher;

    public PaymentService(PaymentRepository paymentRepository, DomainEventPublisher eventPublisher) {
        this.paymentRepository = paymentRepository;
        this.eventPublisher = eventPublisher;
    }

    public Payment process(Long orderId, BigDecimal amount, String method) {
        Payment payment = new Payment();
        payment.setOrderId(orderId);
        payment.setAmount(amount);
        payment.setMethod(method);
        Payment saved = paymentRepository.save(payment);
        eventPublisher.publish("payment.succeeded", "order=" + orderId + ",payment=" + saved.getId());
        return saved;
    }

    public PaymentReceipt receipt(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Payment not found"));
        return toReceipt(payment);
    }

    public CheckoutSessionResponse createCheckoutSession(CheckoutSessionRequest request) {
        String sessionId = "sim_" + UUID.randomUUID();
        String checkoutUrl = "/payments/simulated-checkout/" + sessionId;
        eventPublisher.publish("payment.checkout-session-created", "order=" + request.orderId() + ",session=" + sessionId);
        return new CheckoutSessionResponse(sessionId, "SIMULATED_GATEWAY", checkoutUrl, request.amount(), request.paymentMethod());
    }

    public PaymentReceipt toReceipt(Payment payment) {
        return new PaymentReceipt(payment.getId(), payment.getOrderId(), payment.getAmount(), payment.getMethod(),
                payment.getStatus(), payment.getTransactionReference(), payment.getPaidAt());
    }
}
