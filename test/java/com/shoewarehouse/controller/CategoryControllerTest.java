package com.shoewarehouse.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shoewarehouse.entity.Category;
import com.shoewarehouse.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoryController.class)
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @Autowired
    private ObjectMapper objectMapper;

    private Category category;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setId(1L);
        category.setName("Sneakers");
        category.setDescription("Casual footwear");
    }

    @Test
    @WithMockUser
    void testGetAllCategories() throws Exception {
        List<Category> categories = Arrays.asList(category);
        when(categoryService.getAllCategories()).thenReturn(categories);

        mockMvc.perform(get("/api/categories")
)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Sneakers"));

        verify(categoryService).getAllCategories();
    }

    @Test
    @WithMockUser
    void testGetCategoryById() throws Exception {
        when(categoryService.getCategoryById(1L)).thenReturn(Optional.of(category));

        mockMvc.perform(get("/api/categories/1")
)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Sneakers"));

        verify(categoryService).getCategoryById(1L);
    }

    @Test
    @WithMockUser
    void testGetCategoryByIdNotFound() throws Exception {
        when(categoryService.getCategoryById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/categories/1")
)
                .andExpect(status().isNotFound());

        verify(categoryService).getCategoryById(1L);
    }

    @Test
    @WithMockUser
    void testCreateCategory() throws Exception {
        when(categoryService.createCategory(any(Category.class))).thenReturn(category);

        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(category)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Sneakers"));

        verify(categoryService).createCategory(any(Category.class));
    }

    @Test
    @WithMockUser
    void testUpdateCategory() throws Exception {
        when(categoryService.updateCategory(eq(1L), any(Category.class))).thenReturn(category);

        mockMvc.perform(put("/api/categories/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(category)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Sneakers"));

        verify(categoryService).updateCategory(eq(1L), any(Category.class));
    }

    @Test
    @WithMockUser
    void testDeleteCategory() throws Exception {
        doNothing().when(categoryService).deleteCategory(1L);

        mockMvc.perform(delete("/api/categories/1")
)
                .andExpect(status().isNoContent());

        verify(categoryService).deleteCategory(1L);
    }
}
