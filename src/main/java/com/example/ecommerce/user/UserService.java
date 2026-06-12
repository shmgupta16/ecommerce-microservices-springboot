package com.example.ecommerce.user;

import com.example.ecommerce.common.events.DomainEventPublisher;
import com.example.ecommerce.common.security.CurrentUser;
import com.example.ecommerce.common.security.JwtService;
import com.example.ecommerce.user.dto.AuthDtos.AuthResponse;
import com.example.ecommerce.user.dto.AuthDtos.LoginRequest;
import com.example.ecommerce.user.dto.AuthDtos.PasswordResetResponse;
import com.example.ecommerce.user.dto.AuthDtos.RegisterRequest;
import com.example.ecommerce.user.dto.AuthDtos.UpdateProfileRequest;
import com.example.ecommerce.user.dto.AuthDtos.UserResponse;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final DomainEventPublisher eventPublisher;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService,
                       DomainEventPublisher eventPublisher) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.eventPublisher = eventPublisher;
    }

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email is already registered");
        }
        AppUser user = new AppUser();
        user.setEmail(request.email().toLowerCase());
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setFullName(request.fullName());
        AppUser saved = userRepository.save(user);
        eventPublisher.publish("user.registered", saved.getEmail());
        return new AuthResponse(jwtService.issue(saved.getEmail()), UserMapper.toResponse(saved));
    }

    public AuthResponse login(LoginRequest request) {
        AppUser user = userRepository.findByEmail(request.email().toLowerCase())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password"));
        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
        }
        return new AuthResponse(jwtService.issue(user.getEmail()), UserMapper.toResponse(user));
    }

    public UserResponse me(CurrentUser currentUser) {
        return UserMapper.toResponse(load(currentUser.id()));
    }

    public UserResponse update(CurrentUser currentUser, UpdateProfileRequest request) {
        AppUser user = load(currentUser.id());
        user.setFullName(request.fullName());
        user.setPhone(request.phone());
        user.setAddress(request.address());
        return UserMapper.toResponse(userRepository.save(user));
    }

    public PasswordResetResponse createResetToken(String email) {
        userRepository.findByEmail(email.toLowerCase())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No user found for email"));
        String token = UUID.randomUUID().toString();
        eventPublisher.publish("user.password-reset.requested", email);
        return new PasswordResetResponse("Password reset link generated", token);
    }

    public AppUser load(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }
}
