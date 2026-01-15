package com.shoewarehouse.service;

import com.shoewarehouse.entity.Supplier;
import com.shoewarehouse.repository.SupplierRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SupplierService {

    @Autowired
    private SupplierRepository supplierRepository;

    public List<Supplier> getAllSuppliers() {
        return supplierRepository.findAll();
    }

    public Optional<Supplier> getSupplierById(Long id) {
        return supplierRepository.findById(id);
    }

    public Supplier createSupplier(Supplier supplier) {
        if (supplierRepository.existsByEmail(supplier.getEmail())) {
            throw new RuntimeException("Supplier with email " + supplier.getEmail() + " already exists");
        }
        return supplierRepository.save(supplier);
    }

    public Supplier updateSupplier(Long id, Supplier supplierDetails) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Supplier not found with id: " + id));
        
        if (supplierDetails.getName() != null) {
            supplier.setName(supplierDetails.getName());
        }
        if (supplierDetails.getEmail() != null && !supplierDetails.getEmail().equals(supplier.getEmail())) {
            if (supplierRepository.existsByEmail(supplierDetails.getEmail())) {
                throw new RuntimeException("Supplier with email " + supplierDetails.getEmail() + " already exists");
            }
            supplier.setEmail(supplierDetails.getEmail());
        }
        if (supplierDetails.getPhone() != null) {
            supplier.setPhone(supplierDetails.getPhone());
        }
        if (supplierDetails.getAddress() != null) {
            supplier.setAddress(supplierDetails.getAddress());
        }
        if (supplierDetails.getCity() != null) {
            supplier.setCity(supplierDetails.getCity());
        }
        if (supplierDetails.getCountry() != null) {
            supplier.setCountry(supplierDetails.getCountry());
        }
        
        return supplierRepository.save(supplier);
    }

    public void deleteSupplier(Long id) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Supplier not found with id: " + id));
        supplierRepository.delete(supplier);
    }
}
