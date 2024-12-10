package com.ndc.deliverymanagement.service;

import com.ndc.deliverymanagement.dto.OrderHistoryDTO;
import com.ndc.deliverymanagement.model.Order;
import com.ndc.deliverymanagement.model.OrderHistory;
import com.ndc.deliverymanagement.model.Shipper;
import com.ndc.deliverymanagement.model.User;
import com.ndc.deliverymanagement.repository.OrderHistoryRepository;
import com.ndc.deliverymanagement.repository.OrderRepository;
import com.ndc.deliverymanagement.repository.ShipperRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {
    @Autowired
    public OrderRepository orderRepository;

    @Autowired
    public OrderHistoryRepository orderHistoryRepository;

    @Autowired
    public ShipperRepository shipperRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order save(Order order) {
        return orderRepository.save(order);

    }
    public List<Order> findOrdersBySenderPhoneNumber(String senderPhoneNumber) {
        return orderRepository.findBySenderPhoneNumber(senderPhoneNumber);
    }

    public List<Order> findOrdersByReceiverPhoneNumber(String receiverPhoneNumber) {
        return orderRepository.findByReceiverPhoneNumber(receiverPhoneNumber);
    }

    public List<Order> findByPickupShipperPhoneNumber(String pickupShipperPhoneNumber) {
        return orderRepository.findByPickupShipperPhoneNumber(pickupShipperPhoneNumber);
    }
    public List<Order> findByDeliverShipperPhoneNumber(String deliverShipperPhoneNumber) {
        return orderRepository.findByDeliverShipperPhoneNumber(deliverShipperPhoneNumber);
    }
    public List<Order> findAll() {
        return orderRepository.findAll();
    }
    public int countOrdersByStatus(String currentStatus) {
        return orderRepository.countByCurrentStatus(currentStatus);

    }

    @Transactional
    public void updateOrderStatus(Long orderId, String newStatus, String description) {
        // Tìm đơn hàng
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng."));
        // Cập nhật trạng thái hiện tại
        order.setCurrentStatus(newStatus);
        orderRepository.save(order);
        // Thêm bản ghi vào OrderHistory
        OrderHistory history = new OrderHistory();
        history.setOrder(order);
        history.setStatus(newStatus);
        history.setDescription(description);
        history.setTimestamp(LocalDateTime.now());
        orderHistoryRepository.save(history);
    }

    public List<OrderHistoryDTO> getOrderHistory(Long orderId) {
        List<OrderHistory> history = orderHistoryRepository.findByOrderId(orderId);

        // Chuyển đổi danh sách `OrderHistory` thành danh sách `OrderHistoryDTO`
        return history.stream()
                .map(h -> new OrderHistoryDTO(
                        h.getStatus(),
                        h.getDescription(),
                        h.getTimestamp()))
                .collect(Collectors.toList());
    }

    @Transactional
    public Order assignPickupShipper(Long orderId, Long shipperId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng."));

        Shipper shipper = shipperRepository.findById(shipperId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy shipper."));

        order.setPickupShipperPhoneNumber(shipper.getPhoneNumber());
        return orderRepository.save(order);
    }
    @Transactional
    public Order assignDeliverShipper(Long orderId, Long shipperId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng."));

        Shipper shipper = shipperRepository.findById(shipperId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy shipper."));

        order.setDeliverShipperPhoneNumber(shipper.getPhoneNumber());
        return orderRepository.save(order);
    }
    public double calculateShippingCost(double orderWeight, int orderQuantity) {
        // Logic tính chi phí dựa trên trọng lượng, số lượng và địa chỉ
        double baseCost = 10000; // Chi phí cơ bản
        double orderWeightCost = orderWeight * 2000; // Chi phí dựa trên trọng lượng (giả sử 1kg = 1000 VNĐ)
        double orderQuantityCost = orderQuantity * 3500; // Chi phí dựa trên số lượng (giả sử mỗi món hàng thêm 500 VNĐ)

        return baseCost + orderWeightCost + orderQuantityCost;
    }

}
