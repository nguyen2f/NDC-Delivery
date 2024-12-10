package com.ndc.deliverymanagement.repository;

import com.ndc.deliverymanagement.model.Manager;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManagerRepository extends JpaRepository<Manager, Long> {
    Manager findByPhoneNumber(String phoneNumber);
}
