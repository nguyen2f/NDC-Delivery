package com.ndc.deliverymanagement.service;

import com.ndc.deliverymanagement.model.User;
import com.ndc.deliverymanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User save(User user) {
        return userRepository.save(user);
    }

    public User findByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber);
    }

    public boolean checkLogin(String phoneNumber, String password) {
        User user = findByPhoneNumber(phoneNumber);
        return user != null && user.getPassword().equals(password);
    }
}