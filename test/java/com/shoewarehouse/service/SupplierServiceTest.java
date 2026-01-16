package com.shoewarehouse.service;

import com.shoewarehouse.entity.Supplier;
import com.shoewarehouse.repository.SupplierRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SupplierServiceTest {

    @Mock
    private SupplierRepository supplierRepository;

    @InjectMocks
    private SupplierService supplierService;

    private Supplier supplier;

    @BeforeEach
    void setUp() {
        supplier = new Supplier();
        supplier.setId(1L);
        supplier.setName("ABC Suppliers");
        supplier.setEmail("info@abc.com");
        supplier.setPhone("+359888123456");
        supplier.setAddress("Main Street 123");
        supplier.setCity("Sofia");
        supplier.setCountry("Bulgaria");
    }

    @Test
    void testGetAllSuppliers() {
        List<Supplier> suppliers = Arrays.asList(supplier);
        when(supplierRepository.findAll()).thenReturn(suppliers);

        List<Supplier> result = supplierService.getAllSuppliers();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("ABC Suppliers");
        verify(supplierRepository).findAll();
    }

    @Test
    void testGetSupplierById() {
        when(supplierRepository.findById(1L)).thenReturn(Optional.of(supplier));

        Optional<Supplier> result = supplierService.getSupplierById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("ABC Suppliers");
        verify(supplierRepository).findById(1L);
    }

    @Test
    void testCreateSupplier() {
        when(supplierRepository.existsByEmail("info@abc.com")).thenReturn(false);
        when(supplierRepository.save(any(Supplier.class))).thenReturn(supplier);

        Supplier result = supplierService.createSupplier(supplier);

        assertThat(result.getName()).isEqualTo("ABC Suppliers");
        verify(supplierRepository).existsByEmail("info@abc.com");
        verify(supplierRepository).save(supplier);
    }

    @Test
    void testCreateSupplierWithDuplicateEmail() {
        when(supplierRepository.existsByEmail("info@abc.com")).thenReturn(true);

        assertThatThrownBy(() -> supplierService.createSupplier(supplier))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("already exists");

        verify(supplierRepository, never()).save(any(Supplier.class));
    }

    @Test
    void testUpdateSupplier() {
        Supplier updatedSupplier = new Supplier();
        updatedSupplier.setName("ABC Suppliers Updated");
        updatedSupplier.setEmail("newemail@abc.com");

        when(supplierRepository.findById(1L)).thenReturn(Optional.of(supplier));
        when(supplierRepository.existsByEmail("newemail@abc.com")).thenReturn(false);
        when(supplierRepository.save(any(Supplier.class))).thenReturn(supplier);

        Supplier result = supplierService.updateSupplier(1L, updatedSupplier);

        assertThat(result).isNotNull();
        verify(supplierRepository).findById(1L);
        verify(supplierRepository).save(any(Supplier.class));
    }

    @Test
    void testUpdateSupplierNotFound() {
        when(supplierRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> supplierService.updateSupplier(1L, supplier))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("not found");
    }

    @Test
    void testDeleteSupplier() {
        when(supplierRepository.findById(1L)).thenReturn(Optional.of(supplier));
        doNothing().when(supplierRepository).delete(supplier);

        supplierService.deleteSupplier(1L);

        verify(supplierRepository).findById(1L);
        verify(supplierRepository).delete(supplier);
    }

    @Test
    void testDeleteSupplierNotFound() {
        when(supplierRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> supplierService.deleteSupplier(1L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("not found");
    }
}
