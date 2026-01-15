package com.shoewarehouse.repository;

import com.shoewarehouse.entity.Brand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class BrandRepositoryTest {

    @Autowired
    private BrandRepository brandRepository;

    private Brand brand;

    @BeforeEach
    void setUp() {
        brand = new Brand();
        brand.setName("Nike");
        brand.setDescription("Sportswear brand");
        brand.setCountry("USA");
    }

    @Test
    void testSaveBrand() {
        Brand savedBrand = brandRepository.save(brand);
        assertThat(savedBrand.getId()).isNotNull();
        assertThat(savedBrand.getName()).isEqualTo("Nike");
    }

    @Test
    void testFindBrandById() {
        Brand savedBrand = brandRepository.save(brand);
        Optional<Brand> foundBrand = brandRepository.findById(savedBrand.getId());
        
        assertThat(foundBrand).isPresent();
        assertThat(foundBrand.get().getName()).isEqualTo("Nike");
    }

    @Test
    void testFindBrandByName() {
        brandRepository.save(brand);
        Optional<Brand> foundBrand = brandRepository.findByName("Nike");
        
        assertThat(foundBrand).isPresent();
        assertThat(foundBrand.get().getName()).isEqualTo("Nike");
    }

    @Test
    void testExistsByName() {
        brandRepository.save(brand);
        boolean exists = brandRepository.existsByName("Nike");
        assertThat(exists).isTrue();
    }

    @Test
    void testUpdateBrand() {
        Brand savedBrand = brandRepository.save(brand);
        savedBrand.setDescription("Updated description");
        Brand updatedBrand = brandRepository.save(savedBrand);
        
        assertThat(updatedBrand.getDescription()).isEqualTo("Updated description");
    }

    @Test
    void testDeleteBrand() {
        Brand savedBrand = brandRepository.save(brand);
        brandRepository.delete(savedBrand);
        
        Optional<Brand> foundBrand = brandRepository.findById(savedBrand.getId());
        assertThat(foundBrand).isEmpty();
    }
}
