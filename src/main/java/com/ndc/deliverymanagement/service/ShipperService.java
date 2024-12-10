package com.ndc.deliverymanagement.service;

import com.ndc.deliverymanagement.model.Order;
import com.ndc.deliverymanagement.model.Shipper;
import com.ndc.deliverymanagement.model.User;
import com.ndc.deliverymanagement.repository.ShipperRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShipperService {

    @Autowired
    private ShipperRepository shipperRepository;

    public ShipperService(ShipperRepository shipperRepository) {
        this.shipperRepository = shipperRepository;
    }

    public boolean checkLogin(String phoneNumber, String password) {
        Shipper shipper = findByPhoneNumber(phoneNumber);
        return shipper != null && shipper.getPassword().equals(password);
    }
    public void updateStatus(String phoneNumber, String Status) {
        Shipper shipper = shipperRepository.findByPhoneNumber(phoneNumber);
        shipper.setStatus(Status);
        shipperRepository.save(shipper);
    }

    public Shipper findByPhoneNumber(String phoneNumber) {
        return shipperRepository.findByPhoneNumber(phoneNumber);
    }
    // Get all shippers
    public List<Shipper> getAllShippers() {
        return shipperRepository.findAll();
    }

    // Create a new shipper
    public Shipper createShipper(Shipper shipper) {
        return shipperRepository.save(shipper);
    }

    // Update shipper details
    public Shipper updateShipper(Long id, Shipper shipperDetails) throws Exception {
        Shipper shipper = shipperRepository.findById(id).orElseThrow(() -> new Exception("Shipper not found"));
        shipper.setFullName(shipperDetails.getFullName());
        shipper.setPhoneNumber(shipperDetails.getPhoneNumber());
        return shipperRepository.save(shipper);
    }

    // Delete a shipper
    public void deleteShipper(Long id) throws Exception {
        Shipper shipper = shipperRepository.findById(id).orElseThrow(() -> new Exception("Shipper not found"));
        shipperRepository.delete(shipper);
    }

}
