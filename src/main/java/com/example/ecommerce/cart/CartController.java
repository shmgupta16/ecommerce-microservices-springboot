package com.example.ecommerce.cart;

import com.example.ecommerce.cart.dto.CartDtos.AddCartItemRequest;
import com.example.ecommerce.cart.dto.CartDtos.CartResponse;
import com.example.ecommerce.cart.dto.CartDtos.UpdateCartItemRequest;
import com.example.ecommerce.common.security.CurrentUser;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    CartResponse getCart(@AuthenticationPrincipal CurrentUser user) {
        return cartService.getCart(user);
    }

    @PostMapping("/items")
    CartResponse add(@AuthenticationPrincipal CurrentUser user, @Valid @RequestBody AddCartItemRequest request) {
        return cartService.add(user, request);
    }

    @PutMapping("/items/{productId}")
    CartResponse update(@AuthenticationPrincipal CurrentUser user, @PathVariable Long productId,
                        @Valid @RequestBody UpdateCartItemRequest request) {
        return cartService.update(user, productId, request.quantity());
    }

    @DeleteMapping("/items/{productId}")
    CartResponse remove(@AuthenticationPrincipal CurrentUser user, @PathVariable Long productId) {
        return cartService.remove(user, productId);
    }
}
