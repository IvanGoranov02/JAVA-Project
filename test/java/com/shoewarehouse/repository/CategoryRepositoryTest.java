package com.shoewarehouse.repository;

import com.shoewarehouse.entity.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    private Category category;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setName("Sneakers");
        category.setDescription("Casual sneakers");
    }

    @Test
    void testSaveCategory() {
        Category savedCategory = categoryRepository.save(category);
        assertThat(savedCategory.getId()).isNotNull();
        assertThat(savedCategory.getName()).isEqualTo("Sneakers");
    }

    @Test
    void testFindCategoryById() {
        Category savedCategory = categoryRepository.save(category);
        Optional<Category> foundCategory = categoryRepository.findById(savedCategory.getId());
        
        assertThat(foundCategory).isPresent();
        assertThat(foundCategory.get().getName()).isEqualTo("Sneakers");
    }

    @Test
    void testFindCategoryByName() {
        categoryRepository.save(category);
        Optional<Category> foundCategory = categoryRepository.findByName("Sneakers");
        
        assertThat(foundCategory).isPresent();
        assertThat(foundCategory.get().getName()).isEqualTo("Sneakers");
    }

    @Test
    void testUpdateCategory() {
        Category savedCategory = categoryRepository.save(category);
        savedCategory.setDescription("Updated description");
        Category updatedCategory = categoryRepository.save(savedCategory);
        
        assertThat(updatedCategory.getDescription()).isEqualTo("Updated description");
    }

    @Test
    void testDeleteCategory() {
        Category savedCategory = categoryRepository.save(category);
        categoryRepository.delete(savedCategory);
        
        Optional<Category> foundCategory = categoryRepository.findById(savedCategory.getId());
        assertThat(foundCategory).isEmpty();
    }
}
