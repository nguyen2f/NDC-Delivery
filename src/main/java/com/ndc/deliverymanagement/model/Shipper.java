package com.ndc.deliverymanagement.model;

import jakarta.persistence.Entity;

@Entity
public class Shipper extends User{
    public Shipper(String fullName, String phoneNumber, String password) {
        super(fullName, phoneNumber, password);
    }
}