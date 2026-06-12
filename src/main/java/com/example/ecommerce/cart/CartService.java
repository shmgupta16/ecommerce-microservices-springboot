package com.example.ecommerce.cart;

import com.example.ecommerce.cart.dto.CartDtos.AddCartItemRequest;
import com.example.ecommerce.cart.dto.CartDtos.CartItemResponse;
import com.example.ecommerce.cart.dto.CartDtos.CartResponse;
import com.example.ecommerce.catalog.CatalogService;
import com.example.ecommerce.catalog.Product;
import com.example.ecommerce.common.events.DomainEventPublisher;
import com.example.ecommerce.common.security.CurrentUser;
import com.example.ecommerce.user.UserService;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CartService {
    private final CartItemRepository cartItemRepository;
    private final CatalogService catalogService;
    private final UserService userService;
    private final DomainEventPublisher eventPublisher;

    public CartService(CartItemRepository cartItemRepository, CatalogService catalogService, UserService userService,
                       DomainEventPublisher eventPublisher) {
        this.cartItemRepository = cartItemRepository;
        this.catalogService = catalogService;
        this.userService = userService;
        this.eventPublisher = eventPublisher;
    }

    public CartResponse getCart(CurrentUser user) {
        return toCartResponse(cartItemRepository.findByUserId(user.id()));
    }

    public CartResponse add(CurrentUser user, AddCartItemRequest request) {
        Product product = catalogService.load(request.productId());
        CartItem item = cartItemRepository.findByUserIdAndProductId(user.id(), request.productId()).orElseGet(CartItem::new);
        item.setUser(userService.load(user.id()));
        item.setProduct(product);
        item.setQuantity(item.getQuantity() + request.quantity());
        cartItemRepository.save(item);
        eventPublisher.publish("cart.item-added", "user=" + user.email() + ",product=" + product.getId());
        return getCart(user);
    }

    public CartResponse update(CurrentUser user, Long productId, int quantity) {
        CartItem item = cartItemRepository.findByUserIdAndProductId(user.id(), productId)
                .orElseThrow(() -> new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "Cart item not found"));
        item.setQuantity(quantity);
        cartItemRepository.save(item);
        return getCart(user);
    }

    public CartResponse remove(CurrentUser user, Long productId) {
        cartItemRepository.findByUserIdAndProductId(user.id(), productId).ifPresent(cartItemRepository::delete);
        return getCart(user);
    }

    @Transactional
    public void clear(Long userId) {
        cartItemRepository.deleteByUserId(userId);
    }

    public List<CartItem> items(Long userId) {
        return cartItemRepository.findByUserId(userId);
    }

    private CartResponse toCartResponse(List<CartItem> items) {
        List<CartItemResponse> lines = items.stream()
                .map(item -> {
                    BigDecimal lineTotal = item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
                    return new CartItemResponse(item.getProduct().getId(), item.getProduct().getName(),
                            item.getProduct().getPrice(), item.getQuantity(), lineTotal);
                }).toList();
        BigDecimal total = lines.stream().map(CartItemResponse::lineTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
        return new CartResponse(lines, total);
    }
}
