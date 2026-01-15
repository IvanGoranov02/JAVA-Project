package com.shoewarehouse.repository;

import com.shoewarehouse.entity.Brand;
import com.shoewarehouse.entity.Category;
import com.shoewarehouse.entity.Shoe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class ShoeRepositoryTest {

    @Autowired
    private ShoeRepository shoeRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private Brand brand;
    private Category category;
    private Shoe shoe;

    @BeforeEach
    void setUp() {
        brand = new Brand();
        brand.setName("Nike");
        brand = brandRepository.save(brand);

        category = new Category();
        category.setName("Sneakers");
        category = categoryRepository.save(category);

        shoe = new Shoe();
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
    void testSaveShoe() {
        Shoe savedShoe = shoeRepository.save(shoe);
        assertThat(savedShoe.getId()).isNotNull();
        assertThat(savedShoe.getModel()).isEqualTo("Air Max");
        assertThat(savedShoe.getBrand().getName()).isEqualTo("Nike");
    }

    @Test
    void testFindShoeById() {
        Shoe savedShoe = shoeRepository.save(shoe);
        Shoe foundShoe = shoeRepository.findById(savedShoe.getId()).orElse(null);
        
        assertThat(foundShoe).isNotNull();
        assertThat(foundShoe.getModel()).isEqualTo("Air Max");
    }

    @Test
    void testFindShoesByBrand() {
        shoeRepository.save(shoe);
        List<Shoe> shoes = shoeRepository.findByBrandId(brand.getId());
        
        assertThat(shoes).isNotEmpty();
        assertThat(shoes.get(0).getBrand().getId()).isEqualTo(brand.getId());
    }

    @Test
    void testFindShoesByCategory() {
        shoeRepository.save(shoe);
        List<Shoe> shoes = shoeRepository.findByCategoryId(category.getId());
        
        assertThat(shoes).isNotEmpty();
    }

    @Test
    void testFindLowStockShoes() {
        shoeRepository.save(shoe);
        List<Shoe> lowStockShoes = shoeRepository.findByQuantityLessThan(15);
        
        assertThat(lowStockShoes).isNotEmpty();
        assertThat(lowStockShoes.get(0).getQuantity()).isLessThan(15);
    }

    @Test
    void testUpdateShoe() {
        Shoe savedShoe = shoeRepository.save(shoe);
        savedShoe.setQuantity(20);
        Shoe updatedShoe = shoeRepository.save(savedShoe);
        
        assertThat(updatedShoe.getQuantity()).isEqualTo(20);
    }

    @Test
    void testDeleteShoe() {
        Shoe savedShoe = shoeRepository.save(shoe);
        shoeRepository.delete(savedShoe);
        
        assertThat(shoeRepository.findById(savedShoe.getId())).isEmpty();
    }
}
