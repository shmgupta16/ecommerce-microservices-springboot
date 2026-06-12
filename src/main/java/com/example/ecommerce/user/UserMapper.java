package com.example.ecommerce.user;

import com.example.ecommerce.user.dto.AuthDtos.UserResponse;

public final class UserMapper {
    private UserMapper() {
    }

    public static UserResponse toResponse(AppUser user) {
        return new UserResponse(user.getId(), user.getEmail(), user.getFullName(), user.getPhone(), user.getAddress());
    }
}
