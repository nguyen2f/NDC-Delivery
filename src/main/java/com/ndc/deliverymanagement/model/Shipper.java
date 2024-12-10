package com.ndc.deliverymanagement.model;

import jakarta.persistence.Entity;

@Entity
public class Shipper extends User{
    private String role;
    private String status;

    // Default constructor (JPA requirement)
    public Shipper() {
        super(); // Gọi default constructor của User
    }

    // Constructor đầy đủ tham số
    public Shipper(Long id, String fullName, String phoneNumber, String password, String role, String status) {
        super(id, fullName, phoneNumber, password);
        this.role = role;
        this.status = status;
    }

    // Getters và Setters
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}