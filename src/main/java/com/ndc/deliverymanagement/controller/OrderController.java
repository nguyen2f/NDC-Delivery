package com.ndc.deliverymanagement.controller;

import com.ndc.deliverymanagement.repository.OrderRepository;
import com.ndc.deliverymanagement.service.OrderService;
import com.ndc.deliverymanagement.model.Order;
import com.ndc.deliverymanagement.model.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class OrderController {

    @Autowired
    private OrderService orderService;
    @GetMapping("/create-order")
    public String showCreateOrderForm(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser != null) {
            model.addAttribute("user", loggedInUser);  // Gửi thông tin người dùng hiện tại
            return "create-order";
        }
        return "redirect:/login";  // Nếu chưa đăng nhập, chuyển hướng về trang đăng nhập
    }
    @PostMapping("/create-order")
    public String createOrder(@RequestParam String senderName,
                              @RequestParam String senderPhoneNumber,
                              @RequestParam String senderAddress,
                              @RequestParam String receiverName,
                              @RequestParam String receiverPhoneNumber,
                              @RequestParam String receiverAddress,
                              @RequestParam String itemName,
                              @RequestParam double itemWeight,
                              @RequestParam int itemCount,
                              @RequestParam String distributor,
                              HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return "redirect:/login";
        }
        Order newOrder = new Order();
        newOrder.setSenderName(senderName);
        newOrder.setSenderPhoneNumber(senderPhoneNumber);
        newOrder.setSenderAddress(senderAddress);
        newOrder.setReceiverName(receiverName);
        newOrder.setReceiverPhoneNumber(receiverPhoneNumber);
        newOrder.setReceiverAddress(receiverAddress);
        newOrder.setItemName(itemName);
        newOrder.setItemWeight(itemWeight);
        newOrder.setItemWeight(itemWeight);
        newOrder.setDistributor(distributor);
        newOrder.setCreatedDate(LocalDateTime.now());
        //da xu li xong
        orderService.save(newOrder);

        model.addAttribute("message", "Order created successfully!");
        return "create-order";
    }
    @GetMapping("/manage-order")
    public String manageOrderOptions() {
        return "manage-order";  // Trang hiển thị các lựa chọn: Đơn nhận và Đơn gửi
    }
    @GetMapping("/manage-order/sent")
    public String showSentOrders(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }

        List<Order> sentOrders = orderService.findOrdersBySenderPhone(loggedInUser.getPhoneNumber());
        model.addAttribute("orders", sentOrders);
        model.addAttribute("orderType", "sent");
        return "list-order";
    }

    @GetMapping("/manage-order/received")
    public String showReceivedOrders(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }

        List<Order> receivedOrders = orderService.findOrdersByRecipientPhone(loggedInUser.getPhoneNumber());
        model.addAttribute("orders", receivedOrders);
        model.addAttribute("orderType", "received");
        return "list-order";
    }
}
