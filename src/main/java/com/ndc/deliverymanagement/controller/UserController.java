package com.ndc.deliverymanagement.controller;

import com.ndc.deliverymanagement.dto.UserDTO;
import com.ndc.deliverymanagement.util.JwtUtil;
import com.ndc.deliverymanagement.model.Order;
import com.ndc.deliverymanagement.model.User;
import com.ndc.deliverymanagement.service.UserService;
import com.ndc.deliverymanagement.util.JwtUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private final UserService userService;

    private final JwtUtil jwtUtil;

    public UserController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    // API đăng ký người dùng
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO) {
        // Kiểm tra xem số điện thoại đã tồn tại chưa
        if (userService.findByPhoneNumber(userDTO.getPhoneNumber()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Phone number already registered.");
        }

        // Kiểm tra mật khẩu có hợp lệ không
        if (userDTO.getPassword() == null || userDTO.getPassword().length() < 6) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password must be at least 6 characters.");
        }

        // Đăng ký người dùng
        userService.registerUser(userDTO);
        return ResponseEntity.ok("User registered successfully.");
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestParam String phoneNumber, @RequestParam String password) {
        Optional<User> optionalUser = userService.findByPhoneNumber(phoneNumber); // Tìm người dùng theo số điện thoại

        if (optionalUser.isEmpty() || !userService.checkLogin(phoneNumber, password)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid phone number or password"); // Nếu thông tin sai
        }

        User user = optionalUser.get();
        String token = jwtUtil.generateToken(user); // Tạo token cho người dùng

        return ResponseEntity.ok(Collections.singletonMap("token", token)); // Trả về token cho client
    }



  /*  // API cập nhật thông tin người dùng (yêu cầu xác thực JWT)
    @PutMapping("/update-user")
    public ResponseEntity<String> updateUser(@RequestBody User updatedUser,
                                             @RequestHeader("Authorization") String token) {
        String phoneNumber = jwtUtil.extractPhoneNumber(token.replace("Bearer ", ""));
        User loggedInUser = userService.findByPhoneNumber(phoneNumber);

        if (loggedInUser != null) {
            loggedInUser.setFullName(updatedUser.getFullName());
            loggedInUser.setPassword(updatedUser.getPassword());
            userService.save(loggedInUser);
            return ResponseEntity.ok("User information updated successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token.");
        }
    }*/
}
