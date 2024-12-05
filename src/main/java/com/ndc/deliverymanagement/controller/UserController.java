package com.ndc.deliverymanagement.controller;

import com.ndc.deliverymanagement.model.Order;
import com.ndc.deliverymanagement.model.User;
import com.ndc.deliverymanagement.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // API đăng ký người dùng
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        userService.save(user);
        return ResponseEntity.ok("User registered successfully.");
    }

    // API đăng nhập
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestParam String phoneNumber,
                                       @RequestParam String password, HttpSession session) {
        if (userService.checkLogin(phoneNumber, password)) {
            // Lưu thông tin người dùng vào session
            User loggedInUser = userService.findByPhoneNumber(phoneNumber);
            session.setAttribute("loggedInUser", loggedInUser);
            return ResponseEntity.ok(loggedInUser);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid phone number or password.");
        }
    }

    // API lấy thông tin người dùng đã đăng nhập
    @GetMapping("/me")
    public ResponseEntity<?> getLoggedInUser(HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser != null) {
            return ResponseEntity.ok(loggedInUser);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No user is logged in.");
        }
    }

    // API cập nhật thông tin người dùng
    @PutMapping("/update")
    public ResponseEntity<String> updateUser(@RequestBody User updatedUser, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser != null) {
            loggedInUser.setFullName(updatedUser.getFullName());
            loggedInUser.setPassword(updatedUser.getPassword());
            userService.save(loggedInUser);
            return ResponseEntity.ok("User information updated successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No user is logged in.");
        }
    }

    // API đăng xuất
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok("User logged out successfully.");
    }
}
