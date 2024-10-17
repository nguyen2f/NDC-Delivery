package com.ndc.deliverymanagement.service;

import com.ndc.deliverymanagement.model.Order;
import com.ndc.deliverymanagement.model.User;
import com.ndc.deliverymanagement.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    @Autowired
    public OrderRepository orderRepository;

    public Order save(Order order) {
        return orderRepository.save(order);
    }

}
