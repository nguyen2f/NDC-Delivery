package com.ndc.deliverymanagement.controller;

import com.ndc.deliverymanagement.dto.OrderHistoryDTO;
import com.ndc.deliverymanagement.dto.UpdateOrderStatusRequest;
import com.ndc.deliverymanagement.model.*;
import com.ndc.deliverymanagement.service.AuthService;
import com.ndc.deliverymanagement.service.OrderService;
import com.ndc.deliverymanagement.service.UserService;
import com.ndc.deliverymanagement.util.JwtUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;
    @Autowired
    private AuthService authService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/create-order")
    public ResponseEntity<?> createOrder(@RequestBody Order order,
                                         @RequestHeader("Authorization") String authorizationHeader) {
        ResponseEntity<User> authResponse = authService.validateAndGetUser(authorizationHeader);

        if (!authResponse.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.status(authResponse.getStatusCode()).build();
        }

        User user = authResponse.getBody();
        try {
            Order newOrder = new Order();
            newOrder.setSenderName(order.getSenderName());
            newOrder.setSenderPhoneNumber(order.getSenderPhoneNumber());
            newOrder.setSenderAddress(order.getSenderAddress());
            newOrder.setReceiverName(order.getReceiverName());
            newOrder.setReceiverPhoneNumber(order.getReceiverPhoneNumber());
            newOrder.setReceiverAddress(order.getReceiverAddress());
            newOrder.setOrderName(order.getOrderName());
            newOrder.setOrderWeight(order.getOrderWeight());
            newOrder.setOrderQuantity(order.getOrderQuantity());

            double shippingCost = orderService.calculateShippingCost(
                    order.getOrderWeight(),
                    order.getOrderQuantity()
            );
            newOrder.setShippingCost(shippingCost);

            Order savedOrder = orderService.save(newOrder);
            return ResponseEntity.ok(savedOrder);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Order creation failed: " + e.getMessage());
        }
    }
    //user
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/sent-orders")
    public ResponseEntity<?> getSentOrders(@RequestHeader("Authorization") String authorizationHeader) {
        ResponseEntity<User> authResponse = authService.validateAndGetUser(authorizationHeader);

        if (!authResponse.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.status(authResponse.getStatusCode()).build();
        }

        User user = authResponse.getBody();
        List<Order> sentOrders = orderService.findOrdersBySenderPhoneNumber(user.getPhoneNumber());
        return ResponseEntity.ok(sentOrders);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/received-orders")
    public ResponseEntity<?> getReceivedOrders(@RequestHeader("Authorization") String authorizationHeader) {
        ResponseEntity<User> authResponse = authService.validateAndGetUser(authorizationHeader);

        if (!authResponse.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.status(authResponse.getStatusCode()).build();
        }

        User user = authResponse.getBody();
        List<Order> receivedOrders = orderService.findOrdersByReceiverPhoneNumber(user.getPhoneNumber());
        return ResponseEntity.ok(receivedOrders);
    }
    //shipper
    @GetMapping("/pickup-orders")
    public ResponseEntity<?> getPickupOrders(HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        List<Order> receivedOrders = orderService.findByPickupShipperPhoneNumber(loggedInUser.getPhoneNumber());
        return ResponseEntity.ok(receivedOrders);
    }
    //shipper
    @GetMapping("/deliver-orders")
    public ResponseEntity<?> getDeliverOrders(HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        List<Order> receivedOrders = orderService.findByDeliverShipperPhoneNumber(loggedInUser.getPhoneNumber());
        return ResponseEntity.ok(receivedOrders);
    }
    //shipper
    @PutMapping("/{orderId}/update-status")
    public ResponseEntity<String> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestBody UpdateOrderStatusRequest request,
            HttpSession session) {
        Shipper loggedInShipper = (Shipper) session.getAttribute("loggedInShipper");
        if (loggedInShipper == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }
        try {
            orderService.updateOrderStatus(orderId, request.getStatus(), request.getDescription());
            return ResponseEntity.ok("Cập nhật trạng thái đơn hàng thành công.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //all
    @GetMapping("/{orderId}/history")
    public ResponseEntity<List<OrderHistoryDTO>> getOrderHistory(@PathVariable Long orderId, HttpSession session) {
        List<OrderHistoryDTO> history = orderService.getOrderHistory(orderId);
        return ResponseEntity.ok(history);
    }
    //manager
    // Chưa xử lí
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/statistics")
    public ResponseEntity<?> getOrderStatistics(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }
        try {
            int successOrders = orderService.countOrdersByStatus("THÀNH CÔNG");
            int failedOrders = orderService.countOrdersByStatus("THẤT BẠI");

            Map<String, Integer> statistics = new HashMap<>();
            statistics.put("successOrders", successOrders);
            statistics.put("failedOrders", failedOrders);

            return ResponseEntity.ok(statistics);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to fetch order statistics: " + e.getMessage());
        }
    }
    @PostMapping("/assign-pickupshipper")
    public ResponseEntity<?> assignPickupShipper(@RequestParam Long orderId, @RequestParam Long shipperId,HttpSession session) {
        Manager loggedInManager = (Manager) session.getAttribute("loggedInManager");
        if (loggedInManager == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }
        try {
            Order updatedOrder = orderService.assignPickupShipper(orderId, shipperId);
            return ResponseEntity.ok(updatedOrder);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Phân công shipper thất bại: " + e.getMessage());
        }
    }
    @PostMapping("/assign-delivershipper")
    public ResponseEntity<?> assignDeliverShipper(@RequestParam Long orderId, @RequestParam Long shipperId,HttpSession session) {
        Manager loggedInManager = (Manager) session.getAttribute("loggedInManager");
        if (loggedInManager == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }
        try {
            Order updatedOrder = orderService.assignDeliverShipper(orderId, shipperId);
            return ResponseEntity.ok(updatedOrder);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Phân công shipper thất bại: " + e.getMessage());
        }
    }
    //manager
    @GetMapping("/list-orders")
    public ResponseEntity<?> findAll(HttpSession session) {
        Manager loggedInManager = (Manager) session.getAttribute("loggedInManager");
        if (loggedInManager == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }
        List<Order> orders = orderService.findAll();
        return ResponseEntity.ok(orders);
    }
}