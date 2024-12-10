package com.ndc.deliverymanagement.repository;

import com.ndc.deliverymanagement.model.OrderHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderHistoryRepository extends JpaRepository<OrderHistory, Long> {
    List<OrderHistory> findByOrderId(Long orderId);
}

