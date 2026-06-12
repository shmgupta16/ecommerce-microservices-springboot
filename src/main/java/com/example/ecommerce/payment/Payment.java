package com.example.ecommerce.payment;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long orderId;
    private BigDecimal amount;
    private String method;
    private String transactionReference = UUID.randomUUID().toString();
    @Enumerated(EnumType.STRING)
    private PaymentStatus status = PaymentStatus.SUCCESS;
    private Instant paidAt = Instant.now();

    public Long getId() { return id; }
    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public String getMethod() { return method; }
    public void setMethod(String method) { this.method = method; }
    public String getTransactionReference() { return transactionReference; }
    public PaymentStatus getStatus() { return status; }
    public Instant getPaidAt() { return paidAt; }
}
