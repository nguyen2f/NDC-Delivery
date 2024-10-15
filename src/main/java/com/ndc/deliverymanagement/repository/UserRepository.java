package com.ndc.deliverymanagement.repository;

import com.ndc.deliverymanagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByPhoneNumber(String phoneNumer);
}
