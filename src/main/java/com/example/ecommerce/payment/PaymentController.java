package com.example.ecommerce.payment;

import com.example.ecommerce.payment.dto.PaymentDtos.CheckoutSessionRequest;
import com.example.ecommerce.payment.dto.PaymentDtos.CheckoutSessionResponse;
import com.example.ecommerce.payment.dto.PaymentDtos.PaymentReceipt;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/checkout-session")
    CheckoutSessionResponse createCheckoutSession(@Valid @RequestBody CheckoutSessionRequest request) {
        return paymentService.createCheckoutSession(request);
    }

    @GetMapping("/{paymentId}/receipt")
    PaymentReceipt receipt(@PathVariable Long paymentId) {
        return paymentService.receipt(paymentId);
    }
}
