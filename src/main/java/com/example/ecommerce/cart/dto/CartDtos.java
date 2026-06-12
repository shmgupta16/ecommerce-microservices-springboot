package com.example.ecommerce.cart.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

public class CartDtos {
    public record AddCartItemRequest(@NotNull Long productId, @Min(1) int quantity) {}
    public record UpdateCartItemRequest(@Min(1) int quantity) {}
    public record CartItemResponse(Long productId, String productName, BigDecimal unitPrice, int quantity, BigDecimal lineTotal) {}
    public record CartResponse(List<CartItemResponse> items, BigDecimal total) {}
}
