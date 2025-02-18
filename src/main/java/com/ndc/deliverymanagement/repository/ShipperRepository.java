package com.ndc.deliverymanagement.repository;

import com.ndc.deliverymanagement.model.Shipper;
import com.ndc.deliverymanagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShipperRepository extends JpaRepository<Shipper, Long> {
    Shipper findByPhoneNumber(String phoneNumber);
    boolean existsByPhoneNumber(String phoneNumber);
}
