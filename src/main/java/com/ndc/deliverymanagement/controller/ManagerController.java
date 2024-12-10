 package com.ndc.deliverymanagement.controller;

import com.ndc.deliverymanagement.model.Manager;
import com.ndc.deliverymanagement.model.Shipper;
import com.ndc.deliverymanagement.model.User;
import com.ndc.deliverymanagement.service.ManagerService;
import com.ndc.deliverymanagement.service.ShipperService;
import com.ndc.deliverymanagement.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/manager")
public class ManagerController {
    @Autowired
    public ManagerService managerService;
    @Autowired
    public ShipperService shipperService;
    @Autowired
    public UserService userService;

    public ManagerController(ManagerService managerService) {
        this.managerService = managerService;
    }


    // API đăng nhập
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestParam String phoneNumber,
                                       @RequestParam String password, HttpSession session) {
        if (managerService.checkLogin(phoneNumber, password)) {
            // Lưu thông tin người dùng vào session
            Manager loggedInManager = managerService.findByPhoneNumber(phoneNumber);
            session.setAttribute("loggedInManager", loggedInManager);
            return ResponseEntity.ok(loggedInManager);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid phone number or password.");
        }
    }
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok("Manager logged out successfully.");
    }

    // API to get all shippers
    @GetMapping("/list-shippers")
    public ResponseEntity<List<Shipper>> getAllShippers() {
        List<Shipper> shippers = shipperService.getAllShippers();
        return ResponseEntity.ok(shippers);
    }

    // API to create a new shipper
    @PostMapping("/create-shipper")
    public ResponseEntity<?> createShipper(@RequestBody Shipper shipper) {
        try {
            Shipper newShipper = shipperService.createShipper(shipper);
            return ResponseEntity.ok(newShipper);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to create shipper: " + e.getMessage());
        }
    }

    // API to update shipper details
    @PutMapping("/update-shipper/{id}")
    public ResponseEntity<?> updateShipper(@PathVariable Long id, @RequestBody Shipper shipper) {
        try {
            Shipper updatedShipper = shipperService.updateShipper(id, shipper);
            return ResponseEntity.ok(updatedShipper);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to update shipper: " + e.getMessage());
        }
    }

    // API to delete a shipper
    @DeleteMapping("/delete-shipper/{id}")
    public ResponseEntity<?> deleteShipper(@PathVariable Long id) {
        try {
            shipperService.deleteShipper(id);
            return ResponseEntity.ok("Shipper deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to delete shipper: " + e.getMessage());
        }
    }

    @GetMapping("/list-users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
    @PutMapping("/update-user/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User user) {
        try {
            User updatedUser = userService.updateUser(id, user);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to update user: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete-user/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok("User deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to delete user: " + e.getMessage());
        }
    }
}
