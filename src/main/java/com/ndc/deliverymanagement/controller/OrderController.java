package com.ndc.deliverymanagement.controller;

import com.ndc.deliverymanagement.dto.OrderHistoryDTO;
import com.ndc.deliverymanagement.dto.UpdateOrderStatusRequest;
import com.ndc.deliverymanagement.model.OrderHistory;
import com.ndc.deliverymanagement.service.OrderService;
import com.ndc.deliverymanagement.model.Order;
import com.ndc.deliverymanagement.model.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/create-order")
    public ResponseEntity<?> createOrder(@RequestBody Order order,
                                         HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }
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

            Order savedOrder = orderService.save(newOrder);
            return ResponseEntity.ok(savedOrder);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Order creation failed: " + e.getMessage());
        }
    }

    @GetMapping("/sent-orders")
    public ResponseEntity<?> getSentOrders(HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        List<Order> sentOrders = orderService.findOrdersBySenderPhoneNumber(loggedInUser.getPhoneNumber());
        return ResponseEntity.ok(sentOrders);
    }

    @GetMapping("/received-orders")
    public ResponseEntity<?> getReceivedOrders(HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        List<Order> receivedOrders = orderService.findOrdersByReceiverPhoneNumber(loggedInUser.getPhoneNumber());
        return ResponseEntity.ok(receivedOrders);
    }
    @GetMapping("/pickup-orders")
    public ResponseEntity<?> getPickupOrders(HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        List<Order> receivedOrders = orderService.findByPickupShipperPhoneNumber(loggedInUser.getPhoneNumber());
        return ResponseEntity.ok(receivedOrders);
    }
    @GetMapping("/deliver-orders")
    public ResponseEntity<?> getDeliverOrders(HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        List<Order> receivedOrders = orderService.findByDeliverShipperPhoneNumber(loggedInUser.getPhoneNumber());
        return ResponseEntity.ok(receivedOrders);
    }

    @PutMapping("/{orderId}/update-status")
    public ResponseEntity<String> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestBody UpdateOrderStatusRequest request) {
        try {
            orderService.updateOrderStatus(orderId, request.getStatus(), request.getDescription());
            return ResponseEntity.ok("Cập nhật trạng thái đơn hàng thành công.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/{orderId}/history")
    public ResponseEntity<List<OrderHistoryDTO>> getOrderHistory(@PathVariable Long orderId) {
        List<OrderHistoryDTO> history = orderService.getOrderHistory(orderId);
        return ResponseEntity.ok(history);
    }

    @PostMapping("/assign-pickupshipper")
    public ResponseEntity<?> assignPickupShipper(@RequestParam Long orderId, @RequestParam Long shipperId) {
        try {
            Order updatedOrder = orderService.assignPickupShipper(orderId, shipperId);
            return ResponseEntity.ok(updatedOrder);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Phân công shipper thất bại: " + e.getMessage());
        }
    }
    @PostMapping("/assign-delivershipper")
    public ResponseEntity<?> assignDeliverShipper(@RequestParam Long orderId, @RequestParam Long shipperId) {
        try {
            Order updatedOrder = orderService.assignDeliverShipper(orderId, shipperId);
            return ResponseEntity.ok(updatedOrder);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Phân công shipper thất bại: " + e.getMessage());
        }
    }


}