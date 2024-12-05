package com.ndc.deliverymanagement.model;

import jakarta.persistence.Entity;

@Entity
public class Manager extends User{
    public Manager(String fullName, String phoneNumber, String password) {
        super(fullName, phoneNumber, password);
    }
}
