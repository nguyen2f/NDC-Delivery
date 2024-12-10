package com.ndc.deliverymanagement.service;

import com.ndc.deliverymanagement.model.Manager;
import com.ndc.deliverymanagement.model.User;
import com.ndc.deliverymanagement.repository.ManagerRepository;
import com.ndc.deliverymanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ManagerService {
    @Autowired
    private ManagerRepository managerRepository;

    public Manager findByPhoneNumber(String phoneNumber) {
        return managerRepository.findByPhoneNumber(phoneNumber);
    }

    public boolean checkLogin(String phoneNumber, String password) {
        Manager manager = findByPhoneNumber(phoneNumber);
        return manager != null && manager.getPassword().equals(password);
    }
}
