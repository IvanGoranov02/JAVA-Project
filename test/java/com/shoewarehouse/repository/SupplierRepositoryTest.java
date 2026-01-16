package com.shoewarehouse.repository;

import com.shoewarehouse.entity.Supplier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class SupplierRepositoryTest {

    @Autowired
    private SupplierRepository supplierRepository;

    private Supplier supplier;

    @BeforeEach
    void setUp() {
        supplier = new Supplier();
        supplier.setName("ABC Suppliers");
        supplier.setEmail("info@abc.com");
        supplier.setPhone("+359888123456");
        supplier.setAddress("Main Street 123");
        supplier.setCity("Sofia");
        supplier.setCountry("Bulgaria");
    }

    @Test
    void testSaveSupplier() {
        Supplier savedSupplier = supplierRepository.save(supplier);
        assertThat(savedSupplier.getId()).isNotNull();
        assertThat(savedSupplier.getName()).isEqualTo("ABC Suppliers");
    }

    @Test
    void testFindSupplierById() {
        Supplier savedSupplier = supplierRepository.save(supplier);
        Optional<Supplier> foundSupplier = supplierRepository.findById(savedSupplier.getId());

        assertThat(foundSupplier).isPresent();
        assertThat(foundSupplier.get().getName()).isEqualTo("ABC Suppliers");
    }

    @Test
    void testFindSupplierByEmail() {
        supplierRepository.save(supplier);
        Optional<Supplier> foundSupplier = supplierRepository.findByEmail("info@abc.com");

        assertThat(foundSupplier).isPresent();
        assertThat(foundSupplier.get().getEmail()).isEqualTo("info@abc.com");
    }

    @Test
    void testExistsByEmail() {
        supplierRepository.save(supplier);
        boolean exists = supplierRepository.existsByEmail("info@abc.com");
        assertThat(exists).isTrue();
    }

    @Test
    void testUpdateSupplier() {
        Supplier savedSupplier = supplierRepository.save(supplier);
        savedSupplier.setPhone("+359888999999");
        Supplier updatedSupplier = supplierRepository.save(savedSupplier);

        assertThat(updatedSupplier.getPhone()).isEqualTo("+359888999999");
    }

    @Test
    void testDeleteSupplier() {
        Supplier savedSupplier = supplierRepository.save(supplier);
        supplierRepository.delete(savedSupplier);

        Optional<Supplier> foundSupplier = supplierRepository.findById(savedSupplier.getId());
        assertThat(foundSupplier).isEmpty();
    }
}
