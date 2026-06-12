package com.example.ecommerce.order;

import com.example.ecommerce.cart.CartItem;
import com.example.ecommerce.cart.CartService;
import com.example.ecommerce.common.events.DomainEventPublisher;
import com.example.ecommerce.common.security.CurrentUser;
import com.example.ecommerce.order.dto.OrderDtos.CheckoutRequest;
import com.example.ecommerce.order.dto.OrderDtos.OrderItemResponse;
import com.example.ecommerce.order.dto.OrderDtos.OrderResponse;
import com.example.ecommerce.order.dto.OrderDtos.TrackingResponse;
import com.example.ecommerce.payment.Payment;
import com.example.ecommerce.payment.PaymentService;
import com.example.ecommerce.user.UserService;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final UserService userService;
    private final PaymentService paymentService;
    private final DomainEventPublisher eventPublisher;

    public OrderService(OrderRepository orderRepository, CartService cartService, UserService userService,
                        PaymentService paymentService, DomainEventPublisher eventPublisher) {
        this.orderRepository = orderRepository;
        this.cartService = cartService;
        this.userService = userService;
        this.paymentService = paymentService;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public OrderResponse checkout(CurrentUser user, CheckoutRequest request) {
        List<CartItem> cartItems = cartService.items(user.id());
        if (cartItems.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cart is empty");
        }
        CustomerOrder order = new CustomerOrder();
        order.setUser(userService.load(user.id()));
        order.setDeliveryAddress(request.deliveryAddress());
        order.setPaymentMethod(request.paymentMethod());
        BigDecimal total = BigDecimal.ZERO;
        for (CartItem cartItem : cartItems) {
            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProductId(cartItem.getProduct().getId());
            item.setProductName(cartItem.getProduct().getName());
            item.setUnitPrice(cartItem.getProduct().getPrice());
            item.setQuantity(cartItem.getQuantity());
            order.getItems().add(item);
            total = total.add(cartItem.getProduct().getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
        }
        order.setTotalAmount(total);
        CustomerOrder saved = orderRepository.save(order);
        eventPublisher.publish("order.created", "order=" + saved.getId());
        Payment payment = paymentService.process(saved.getId(), saved.getTotalAmount(), saved.getPaymentMethod());
        saved.setStatus(OrderStatus.PAID);
        cartService.clear(user.id());
        return toResponse(saved, payment.getId());
    }

    public List<OrderResponse> history(CurrentUser user) {
        return orderRepository.findByUserIdOrderByCreatedAtDesc(user.id()).stream()
                .map(order -> toResponse(order, null))
                .toList();
    }

    public TrackingResponse tracking(CurrentUser user, Long orderId) {
        CustomerOrder order = loadOwned(user, orderId);
        return new TrackingResponse(order.getId(), order.getStatus(), trackingMessage(order.getStatus()));
    }

    private CustomerOrder loadOwned(CurrentUser user, Long orderId) {
        CustomerOrder order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));
        if (!order.getUser().getId().equals(user.id())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Order does not belong to current user");
        }
        return order;
    }

    private OrderResponse toResponse(CustomerOrder order, Long paymentId) {
        List<OrderItemResponse> items = order.getItems().stream()
                .map(item -> new OrderItemResponse(item.getProductId(), item.getProductName(), item.getUnitPrice(),
                        item.getQuantity(), item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity()))))
                .toList();
        return new OrderResponse(order.getId(), order.getStatus(), order.getTotalAmount(), order.getDeliveryAddress(),
                order.getPaymentMethod(), order.getCreatedAt(), items, paymentId);
    }

    private String trackingMessage(OrderStatus status) {
        return switch (status) {
            case CREATED, PAYMENT_PENDING -> "Your order is being confirmed";
            case PAID -> "Payment received and order is being prepared";
            case PACKED -> "Your order has been packed";
            case SHIPPED -> "Your order is on the way";
            case DELIVERED -> "Your order has been delivered";
            case CANCELLED -> "Your order was cancelled";
        };
    }
}
