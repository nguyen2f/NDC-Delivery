package com.ndc.deliverymanagement.model;

import jakarta.persistence.Entity;

@Entity
public class Customer extends User{
    public Customer(String fullName, String phoneNumber, String password) {
        super(fullName, phoneNumber, password);
    }

}
