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
    private final ShipperService shipperService;

    public ShipperController(ShipperService shipperService) {
        this.shipperService = shipperService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createShipper(@RequestBody Shipper shipper) {
        try {
            Shipper savedShipper = shipperService.createShipper(shipper);
            return ResponseEntity.ok(savedShipper);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Tạo tài khoản shipper thất bại: " + e.getMessage());
        }
    }
}

