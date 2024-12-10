package com.ndc.deliverymanagement.repository;

import com.ndc.deliverymanagement.model.Order;
import com.ndc.deliverymanagement.model.OrderHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findBySenderPhoneNumber(String senderPhoneNumber);
    List<Order> findByReceiverPhoneNumber(String receiverPhoneNumber);
    List<Order> findByPickupShipperPhoneNumber(String pickupShipperPhoneNumber);
    List<Order> findByDeliverShipperPhoneNumber(String deliverShipperPhoneNumber);
    List<Order> findAll();
    int countByCurrentStatus(String currentStatus);
}