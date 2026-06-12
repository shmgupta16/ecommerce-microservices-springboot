package com.example.ecommerce.user;

import com.example.ecommerce.common.security.CurrentUser;
import com.example.ecommerce.user.dto.AuthDtos.PasswordResetRequest;
import com.example.ecommerce.user.dto.AuthDtos.PasswordResetResponse;
import com.example.ecommerce.user.dto.AuthDtos.UpdateProfileRequest;
import com.example.ecommerce.user.dto.AuthDtos.UserResponse;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    UserResponse me(@AuthenticationPrincipal CurrentUser currentUser) {
        return userService.me(currentUser);
    }

    @PutMapping("/me")
    UserResponse update(@AuthenticationPrincipal CurrentUser currentUser, @RequestBody UpdateProfileRequest request) {
        return userService.update(currentUser, request);
    }

    @PostMapping("/password-reset")
    PasswordResetResponse passwordReset(@Valid @RequestBody PasswordResetRequest request) {
        return userService.createResetToken(request.email());
    }
}
