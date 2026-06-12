package com.example.ecommerce.payment;

import com.example.ecommerce.payment.dto.PaymentDtos.PaymentReceipt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/{paymentId}/receipt")
    PaymentReceipt receipt(@PathVariable Long paymentId) {
        return paymentService.receipt(paymentId);
    }
}
