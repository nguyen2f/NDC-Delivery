package com.ndc.deliverymanagement.controller;

import com.ndc.deliverymanagement.service.OrderHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderHistoryController {
    @Autowired
    public OrderHistoryService orderHistoryService;

}
