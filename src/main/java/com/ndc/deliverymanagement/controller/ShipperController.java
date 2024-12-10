package com.ndc.deliverymanagement.controller;

import com.ndc.deliverymanagement.model.Order;
import com.ndc.deliverymanagement.model.OrderHistory;
import com.ndc.deliverymanagement.model.Shipper;
import com.ndc.deliverymanagement.model.User;
import com.ndc.deliverymanagement.service.OrderHistoryService;
import com.ndc.deliverymanagement.service.ShipperService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shipper")
public class ShipperController {
    @Autowired
    private ShipperService shipperService;

    public ShipperController(ShipperService shipperService) {
        this.shipperService = shipperService;
    }
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestParam String phoneNumber,
                                       @RequestParam String password, HttpSession session) {
        if (shipperService.checkLogin(phoneNumber, password)) {
            // Lưu thông tin người dùng vào session
            Shipper loggedInShipper = shipperService.findByPhoneNumber(phoneNumber);
            session.setAttribute("loggedInUser", loggedInShipper);
            return ResponseEntity.ok(loggedInShipper);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid phone number or password.");
        }
    }
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok("Shipper logged out successfully.");
    }
    /*
    public ResponseEntity<?> updateShipper(HttpSession session,
                                           @RequestBody Shipper shipper) {
        Shipper loggedInShipper = (Shipper) session.getAttribute("loggedInShipper");
        try {
            Shipper updatedShipper = shipperService.updateShipper( loggedInShipper.getPhoneNumber());
            return ResponseEntity.ok(updatedShipper);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to update shipper: " + e.getMessage());
        }
    }
    */


}

