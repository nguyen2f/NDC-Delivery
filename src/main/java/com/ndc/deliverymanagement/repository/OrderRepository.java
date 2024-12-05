package com.ndc.deliverymanagement.repository;

import com.ndc.deliverymanagement.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findBySenderPhoneNumber(String senderPhoneNumber);
    List<Order> findByReceiverPhoneNumber(String receiverPhoneNumber);
///    List<Order> findByShipperPhoneNumber(String shipperPhoneNumber);
    List<Order> findByDistributor(String distributor);
}