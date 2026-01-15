package com.shoewarehouse.service;

import com.shoewarehouse.entity.Brand;
import com.shoewarehouse.repository.BrandRepository;
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
class BrandServiceTest {

    @Mock
    private BrandRepository brandRepository;

    @InjectMocks
    private BrandService brandService;

    private Brand brand;

    @BeforeEach
    void setUp() {
        brand = new Brand();
        brand.setId(1L);
        brand.setName("Nike");
        brand.setDescription("Sportswear brand");
        brand.setCountry("USA");
    }

    @Test
    void testGetAllBrands() {
        List<Brand> brands = Arrays.asList(brand);
        when(brandRepository.findAll()).thenReturn(brands);

        List<Brand> result = brandService.getAllBrands();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Nike");
        verify(brandRepository).findAll();
    }

    @Test
    void testGetBrandById() {
        when(brandRepository.findById(1L)).thenReturn(Optional.of(brand));

        Optional<Brand> result = brandService.getBrandById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Nike");
        verify(brandRepository).findById(1L);
    }

    @Test
    void testCreateBrand() {
        when(brandRepository.existsByName("Nike")).thenReturn(false);
        when(brandRepository.save(any(Brand.class))).thenReturn(brand);

        Brand result = brandService.createBrand(brand);

        assertThat(result.getName()).isEqualTo("Nike");
        verify(brandRepository).existsByName("Nike");
        verify(brandRepository).save(brand);
    }

    @Test
    void testCreateBrandWithDuplicateName() {
        when(brandRepository.existsByName("Nike")).thenReturn(true);

        assertThatThrownBy(() -> brandService.createBrand(brand))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("already exists");
        
        verify(brandRepository, never()).save(any(Brand.class));
    }

    @Test
    void testUpdateBrand() {
        Brand updatedBrand = new Brand();
        updatedBrand.setName("Nike Updated");
        updatedBrand.setDescription("Updated description");

        when(brandRepository.findById(1L)).thenReturn(Optional.of(brand));
        when(brandRepository.existsByName("Nike Updated")).thenReturn(false);
        when(brandRepository.save(any(Brand.class))).thenReturn(brand);

        Brand result = brandService.updateBrand(1L, updatedBrand);

        assertThat(result).isNotNull();
        verify(brandRepository).findById(1L);
        verify(brandRepository).save(any(Brand.class));
    }

    @Test
    void testUpdateBrandNotFound() {
        when(brandRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> brandService.updateBrand(1L, brand))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("not found");
    }

    @Test
    void testDeleteBrand() {
        when(brandRepository.findById(1L)).thenReturn(Optional.of(brand));
        doNothing().when(brandRepository).delete(brand);

        brandService.deleteBrand(1L);

        verify(brandRepository).findById(1L);
        verify(brandRepository).delete(brand);
    }

    @Test
    void testDeleteBrandNotFound() {
        when(brandRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> brandService.deleteBrand(1L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("not found");
    }
}
