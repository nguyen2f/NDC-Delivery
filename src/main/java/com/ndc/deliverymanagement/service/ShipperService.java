package com.ndc.deliverymanagement.service;

import com.ndc.deliverymanagement.model.Order;
import com.ndc.deliverymanagement.model.Shipper;
import com.ndc.deliverymanagement.model.User;
import com.ndc.deliverymanagement.repository.ShipperRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class ShipperService {

    @Autowired
    private ShipperRepository shipperRepository;

    public ShipperService(ShipperRepository shipperRepository) {
        this.shipperRepository = shipperRepository;
    }

    public Shipper findByPhoneNumber(String phoneNumber) {
        return shipperRepository.findByPhoneNumber(phoneNumber);
    }
    public Shipper createShipper(Shipper shipper) {
        if (shipperRepository.existsByPhoneNumber(shipper.getPhoneNumber())) {
            throw new RuntimeException("Số điện thoại đã tồn tại.");
        }
        return shipperRepository.save(shipper);
    }

}
