package com.example.ecommerce.order.dto;

import com.example.ecommerce.order.OrderStatus;
import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public class OrderDtos {
    public record CheckoutRequest(@NotBlank String deliveryAddress, @NotBlank String paymentMethod) {}
    public record OrderItemResponse(Long productId, String productName, BigDecimal unitPrice, int quantity, BigDecimal lineTotal) {}
    public record OrderResponse(Long id, OrderStatus status, BigDecimal totalAmount, String deliveryAddress,
                                String paymentMethod, Instant createdAt, List<OrderItemResponse> items, Long paymentId) {}
    public record TrackingResponse(Long orderId, OrderStatus status, String message) {}
}
