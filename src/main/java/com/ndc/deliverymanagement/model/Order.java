package com.ndc.deliverymanagement.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "deliveryOrder")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String senderName;
    private String senderPhoneNumber;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "province", column = @Column(name = "sender_province")),
            @AttributeOverride(name = "district", column = @Column(name = "sender_district")),
            @AttributeOverride(name = "ward", column = @Column(name = "sender_ward")),
            @AttributeOverride(name = "detailedAddress", column = @Column(name = "sender_detailed_address"))
    })
    private Address senderAddress;

    private String receiverName;
    private String receiverPhoneNumber;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "province", column = @Column(name = "receiver_province")),
            @AttributeOverride(name = "district", column = @Column(name = "receiver_district")),
            @AttributeOverride(name = "ward", column = @Column(name = "receiver_ward")),
            @AttributeOverride(name = "detailedAddress", column = @Column(name = "receiver_detailed_address"))
    })
    private Address receiverAddress;

    private String pickupShipperPhoneNumber;
    private String deliverShipperPhoneNumber;

    private String orderName;
    private double orderWeight;
    private int orderQuantity;

    private double shippingCost;


    private String currentStatus;

    public double getShippingCost() {
        return shippingCost;
    }

    public void setShippingCost(double shippingCost) {
        this.shippingCost = shippingCost;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderPhoneNumber() {
        return senderPhoneNumber;
    }

    public void setSenderPhoneNumber(String senderPhoneNumber) {
        this.senderPhoneNumber = senderPhoneNumber;
    }

    public Address getSenderAddress() {
        return senderAddress;
    }

    public void setSenderAddress(Address senderAddress) {
        this.senderAddress = senderAddress;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverPhoneNumber() {
        return receiverPhoneNumber;
    }

    public void setReceiverPhoneNumber(String receiverPhoneNumber) {
        this.receiverPhoneNumber = receiverPhoneNumber;
    }

    public Address getReceiverAddress() {
        return receiverAddress;
    }

    public void setReceiverAddress(Address receiverAddress) {
        this.receiverAddress = receiverAddress;
    }

    public String getPickupShipperPhoneNumber() {
        return pickupShipperPhoneNumber;
    }

    public void setPickupShipperPhoneNumber(String pickupShipperPhoneNumber) {
        this.pickupShipperPhoneNumber = pickupShipperPhoneNumber;
    }

    public String getDeliverShipperPhoneNumber() {
        return deliverShipperPhoneNumber;
    }

    public void setDeliverShipperPhoneNumber(String deliverShipperPhoneNumber) {
        this.deliverShipperPhoneNumber = deliverShipperPhoneNumber;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public double getOrderWeight() {
        return orderWeight;
    }

    public void setOrderWeight(double orderWeight) {
        this.orderWeight = orderWeight;
    }

    public int getOrderQuantity() {
        return orderQuantity;
    }

    public void setOrderQuantity(int orderQuantity) {
        this.orderQuantity = orderQuantity;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }
}
