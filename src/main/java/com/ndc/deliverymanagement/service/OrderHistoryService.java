package com.ndc.deliverymanagement.service;

import com.ndc.deliverymanagement.model.Order;
import com.ndc.deliverymanagement.model.OrderHistory;
import com.ndc.deliverymanagement.repository.OrderHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class OrderHistoryService {
    @Autowired
    private OrderHistoryRepository orderHistoryRepository;
}
