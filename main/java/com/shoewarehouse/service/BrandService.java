package com.shoewarehouse.service;

import com.shoewarehouse.entity.Brand;
import com.shoewarehouse.repository.BrandRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BrandService {

    @Autowired
    private BrandRepository brandRepository;

    public List<Brand> getAllBrands() {
        return brandRepository.findAll();
    }

    public Optional<Brand> getBrandById(Long id) {
        return brandRepository.findById(id);
    }

    public Brand createBrand(Brand brand) {
        if (brandRepository.existsByName(brand.getName())) {
            throw new RuntimeException("Brand with name " + brand.getName() + " already exists");
        }
        return brandRepository.save(brand);
    }

    public Brand updateBrand(Long id, Brand brandDetails) {
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Brand not found with id: " + id));
        
        if (brandDetails.getName() != null && !brandDetails.getName().equals(brand.getName())) {
            if (brandRepository.existsByName(brandDetails.getName())) {
                throw new RuntimeException("Brand with name " + brandDetails.getName() + " already exists");
            }
            brand.setName(brandDetails.getName());
        }
        
        if (brandDetails.getDescription() != null) {
            brand.setDescription(brandDetails.getDescription());
        }
        if (brandDetails.getCountry() != null) {
            brand.setCountry(brandDetails.getCountry());
        }
        
        return brandRepository.save(brand);
    }

    public void deleteBrand(Long id) {
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Brand not found with id: " + id));
        brandRepository.delete(brand);
    }
}
