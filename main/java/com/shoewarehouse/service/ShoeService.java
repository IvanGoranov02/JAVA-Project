package com.shoewarehouse.service;

import com.shoewarehouse.entity.Category;
import com.shoewarehouse.entity.Shoe;
import com.shoewarehouse.repository.BrandRepository;
import com.shoewarehouse.repository.CategoryRepository;
import com.shoewarehouse.repository.ShoeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ShoeService {

    @Autowired
    private ShoeRepository shoeRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Shoe> getAllShoes() {
        return shoeRepository.findAll();
    }

    public Optional<Shoe> getShoeById(Long id) {
        return shoeRepository.findById(id);
    }

    public List<Shoe> getShoesByBrand(Long brandId) {
        return shoeRepository.findByBrandId(brandId);
    }

    public List<Shoe> getShoesByCategory(Long categoryId) {
        return shoeRepository.findByCategoryId(categoryId);
    }

    public List<Shoe> getLowStockShoes(Integer threshold) {
        return shoeRepository.findByQuantityLessThan(threshold);
    }

    public Shoe createShoe(Shoe shoe) {
        // Validate required fields
        if (shoe.getBrand() == null || shoe.getBrand().getId() == null) {
            throw new RuntimeException("Brand is required");
        }
        
        // Load and validate brand exists
        shoe.setBrand(brandRepository.findById(shoe.getBrand().getId())
                .orElseThrow(() -> new RuntimeException("Brand not found with id: " + shoe.getBrand().getId())));
        
        // Load and validate categories if provided
        if (shoe.getCategories() != null && !shoe.getCategories().isEmpty()) {
            List<Category> categories = shoe.getCategories().stream()
                    .map(cat -> {
                        if (cat.getId() == null) {
                            throw new RuntimeException("Category ID is required");
                        }
                        return categoryRepository.findById(cat.getId())
                                .orElseThrow(() -> new RuntimeException("Category not found with id: " + cat.getId()));
                    })
                    .collect(Collectors.toList());
            shoe.setCategories(categories);
        }
        
        return shoeRepository.save(shoe);
    }

    public Shoe updateShoe(Long id, Shoe shoeDetails) {
        Shoe shoe = shoeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Shoe not found with id: " + id));
        
        if (shoeDetails.getModel() != null) {
            shoe.setModel(shoeDetails.getModel());
        }
        if (shoeDetails.getSize() != null) {
            shoe.setSize(shoeDetails.getSize());
        }
        if (shoeDetails.getColor() != null) {
            shoe.setColor(shoeDetails.getColor());
        }
        if (shoeDetails.getQuantity() != null) {
            shoe.setQuantity(shoeDetails.getQuantity());
        }
        if (shoeDetails.getPrice() != null) {
            shoe.setPrice(shoeDetails.getPrice());
        }
        if (shoeDetails.getBrand() != null && shoeDetails.getBrand().getId() != null) {
            shoe.setBrand(brandRepository.findById(shoeDetails.getBrand().getId())
                    .orElseThrow(() -> new RuntimeException("Brand not found with id: " + shoeDetails.getBrand().getId())));
        }
        if (shoeDetails.getCategories() != null) {
            List<Category> categories = shoeDetails.getCategories().stream()
                    .map(cat -> categoryRepository.findById(cat.getId())
                            .orElseThrow(() -> new RuntimeException("Category not found with id: " + cat.getId())))
                    .collect(Collectors.toList());
            shoe.setCategories(categories);
        }
        
        return shoeRepository.save(shoe);
    }

    public void deleteShoe(Long id) {
        Shoe shoe = shoeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Shoe not found with id: " + id));
        shoeRepository.delete(shoe);
    }
}
