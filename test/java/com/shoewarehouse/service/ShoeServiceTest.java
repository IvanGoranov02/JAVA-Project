package com.shoewarehouse.service;

import com.shoewarehouse.entity.Brand;
import com.shoewarehouse.entity.Category;
import com.shoewarehouse.entity.Shoe;
import com.shoewarehouse.repository.BrandRepository;
import com.shoewarehouse.repository.CategoryRepository;
import com.shoewarehouse.repository.ShoeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShoeServiceTest {

    @Mock
    private ShoeRepository shoeRepository;

    @Mock
    private BrandRepository brandRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ShoeService shoeService;

    private Brand brand;
    private Category category;
    private Shoe shoe;

    @BeforeEach
    void setUp() {
        brand = new Brand();
        brand.setId(1L);
        brand.setName("Nike");

        category = new Category();
        category.setId(1L);
        category.setName("Sneakers");

        shoe = new Shoe();
        shoe.setId(1L);
        shoe.setModel("Air Max");
        shoe.setSize("42");
        shoe.setColor("Black");
        shoe.setQuantity(10);
        shoe.setPrice(new BigDecimal("120.00"));
        shoe.setBrand(brand);
        
        List<Category> categories = new ArrayList<>();
        categories.add(category);
        shoe.setCategories(categories);
    }

    @Test
    void testGetAllShoes() {
        List<Shoe> shoes = Arrays.asList(shoe);
        when(shoeRepository.findAll()).thenReturn(shoes);

        List<Shoe> result = shoeService.getAllShoes();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getModel()).isEqualTo("Air Max");
        verify(shoeRepository).findAll();
    }

    @Test
    void testGetShoeById() {
        when(shoeRepository.findById(1L)).thenReturn(Optional.of(shoe));

        Optional<Shoe> result = shoeService.getShoeById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getModel()).isEqualTo("Air Max");
        verify(shoeRepository).findById(1L);
    }

    @Test
    void testCreateShoe() {
        Shoe newShoe = new Shoe();
        newShoe.setModel("Air Max");
        newShoe.setBrand(brand);
        newShoe.setCategories(Arrays.asList(category));

        when(brandRepository.findById(1L)).thenReturn(Optional.of(brand));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(shoeRepository.save(any(Shoe.class))).thenReturn(shoe);

        Shoe result = shoeService.createShoe(newShoe);

        assertThat(result).isNotNull();
        verify(brandRepository).findById(1L);
        verify(categoryRepository).findById(1L);
        verify(shoeRepository).save(any(Shoe.class));
    }

    @Test
    void testCreateShoeWithInvalidBrand() {
        Shoe newShoe = new Shoe();
        newShoe.setBrand(brand);

        when(brandRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> shoeService.createShoe(newShoe))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Brand not found");
    }

    @Test
    void testUpdateShoe() {
        Shoe updatedShoe = new Shoe();
        updatedShoe.setQuantity(20);

        when(shoeRepository.findById(1L)).thenReturn(Optional.of(shoe));
        when(shoeRepository.save(any(Shoe.class))).thenReturn(shoe);

        Shoe result = shoeService.updateShoe(1L, updatedShoe);

        assertThat(result).isNotNull();
        verify(shoeRepository).findById(1L);
        verify(shoeRepository).save(any(Shoe.class));
    }

    @Test
    void testDeleteShoe() {
        when(shoeRepository.findById(1L)).thenReturn(Optional.of(shoe));
        doNothing().when(shoeRepository).delete(shoe);

        shoeService.deleteShoe(1L);

        verify(shoeRepository).findById(1L);
        verify(shoeRepository).delete(shoe);
    }

    @Test
    void testGetShoesByBrand() {
        List<Shoe> shoes = Arrays.asList(shoe);
        when(shoeRepository.findByBrandId(1L)).thenReturn(shoes);

        List<Shoe> result = shoeService.getShoesByBrand(1L);

        assertThat(result).hasSize(1);
        verify(shoeRepository).findByBrandId(1L);
    }

    @Test
    void testGetLowStockShoes() {
        List<Shoe> shoes = Arrays.asList(shoe);
        when(shoeRepository.findByQuantityLessThan(15)).thenReturn(shoes);

        List<Shoe> result = shoeService.getLowStockShoes(15);

        assertThat(result).hasSize(1);
        verify(shoeRepository).findByQuantityLessThan(15);
    }
}
