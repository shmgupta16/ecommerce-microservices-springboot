package com.example.ecommerce.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class AuthDtos {
    public record RegisterRequest(@Email @NotBlank String email, @NotBlank String password, @NotBlank String fullName) {}
    public record LoginRequest(@Email @NotBlank String email, @NotBlank String password) {}
    public record AuthResponse(String token, UserResponse user) {}
    public record UserResponse(Long id, String email, String fullName, String phone, String address) {}
    public record UpdateProfileRequest(String fullName, String phone, String address) {}
    public record PasswordResetRequest(@Email @NotBlank String email) {}
    public record PasswordResetResponse(String message, String resetToken) {}
}
