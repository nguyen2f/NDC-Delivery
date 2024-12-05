package com.ndc.deliverymanagement.service;

import com.ndc.deliverymanagement.model.Order;
import com.ndc.deliverymanagement.model.User;
import com.ndc.deliverymanagement.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    @Autowired
    public OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order save(Order order) {
        return orderRepository.save(order);
    }
    public List<Order> findOrdersBySenderPhoneNumber(String phoneNumber) {
        return orderRepository.findBySenderPhoneNumber(phoneNumber);
    }
    public List<Order> findOrdersByReceiverPhoneNumber(String phoneNumber) {
        return orderRepository.findByReceiverPhoneNumber(phoneNumber);
    }
/*  public List<Order> findOrderByShipperNumber(String phoneNumber) {
        return orderRepository.findByShipperPhoneNumber(phoneNumber);
     }
*/
    public List<Order> findByDistributor(String distributor) {
        return orderRepository.findByDistributor(distributor);
    }



    public List<Order> getOrdersBySender(String senderPhoneNumber) {
        return orderRepository.findBySenderPhoneNumber(senderPhoneNumber);
    }

    public List<Order> getOrdersByReceiver(String receiverPhoneNumber) {
        return orderRepository.findByReceiverPhoneNumber(receiverPhoneNumber);
    }

}
