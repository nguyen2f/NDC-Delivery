package com.ndc.deliverymanagement.service;

import com.ndc.deliverymanagement.model.User;
import com.ndc.deliverymanagement.util.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final JwtUtil jwtUtil;
    private final UserService userService;

    public AuthService(JwtUtil jwtUtil, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    public ResponseEntity<User> validateAndGetUser(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // Trả về HTTP 401 mà không có body
        }

        String token = authorizationHeader.substring(7);
        String phoneNumber = jwtUtil.extractPhoneNumber(token);
        String role = jwtUtil.extractRole(token);

        if (!"ROLE_USER".equalsIgnoreCase(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // HTTP 403 nếu role không hợp lệ
        }

        return userService.findByPhoneNumber(phoneNumber)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()); // HTTP 401 nếu không tìm thấy user
    }

}

