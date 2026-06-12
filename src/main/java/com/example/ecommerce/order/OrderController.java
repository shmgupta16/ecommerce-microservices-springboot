package com.example.ecommerce.order;

import com.example.ecommerce.common.security.CurrentUser;
import com.example.ecommerce.order.dto.OrderDtos.CheckoutRequest;
import com.example.ecommerce.order.dto.OrderDtos.OrderResponse;
import com.example.ecommerce.order.dto.OrderDtos.TrackingResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/checkout")
    OrderResponse checkout(@AuthenticationPrincipal CurrentUser user, @Valid @RequestBody CheckoutRequest request) {
        return orderService.checkout(user, request);
    }

    @GetMapping
    List<OrderResponse> history(@AuthenticationPrincipal CurrentUser user) {
        return orderService.history(user);
    }

    @GetMapping("/{orderId}/tracking")
    TrackingResponse tracking(@AuthenticationPrincipal CurrentUser user, @PathVariable Long orderId) {
        return orderService.tracking(user, orderId);
    }
}
