package com.ndc.deliverymanagement.controller;

import com.ndc.deliverymanagement.service.OrderService;
import com.ndc.deliverymanagement.model.Order;
import com.ndc.deliverymanagement.model.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/create")
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
            newOrder.setItemName(order.getItemName());
            newOrder.setItemWeight(order.getItemWeight());
            newOrder.setItemCount(order.getItemCount());
            newOrder.setDistributor(order.getDistributor());
            newOrder.setCreatedDate(LocalDateTime.now());

            Order savedOrder = orderService.save(newOrder);
            return ResponseEntity.ok(savedOrder);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Order creation failed: " + e.getMessage());
        }
    }

    @GetMapping("/sent")
    public ResponseEntity<?> getSentOrders(HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        List<Order> sentOrders = orderService.findOrdersBySenderPhoneNumber(loggedInUser.getPhoneNumber());
        return ResponseEntity.ok(sentOrders);
    }

    @GetMapping("/received")
    public ResponseEntity<?> getReceivedOrders(HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        List<Order> receivedOrders = orderService.findOrdersByReceiverPhoneNumber(loggedInUser.getPhoneNumber());
        return ResponseEntity.ok(receivedOrders);
    }

    @GetMapping("/distributor")
    public ResponseEntity<?> getDistributorOrders(HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null ||
                (!loggedInUser.getPhoneNumber().equals("0444555666") &&
                        !loggedInUser.getPhoneNumber().equals("0111222333"))) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        List<Order> distributorOrders = orderService.findByDistributor(loggedInUser.getPhoneNumber());
        return ResponseEntity.ok(distributorOrders);
    }




}