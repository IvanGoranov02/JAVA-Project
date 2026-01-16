package com.shoewarehouse.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shoewarehouse.entity.Brand;
import com.shoewarehouse.entity.Category;
import com.shoewarehouse.entity.Shoe;
import com.shoewarehouse.service.ShoeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ShoeController.class)
@AutoConfigureMockMvc(addFilters = false)
class ShoeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ShoeService shoeService;

    @Autowired
    private ObjectMapper objectMapper;

    private Shoe shoe;

    @BeforeEach
    void setUp() {
        Brand brand = new Brand();
        brand.setId(1L);
        brand.setName("Nike");

        Category category = new Category();
        category.setId(1L);
        category.setName("Sneakers");

        shoe = new Shoe();
        shoe.setId(1L);
        shoe.setModel("Air Max");
        shoe.setSize("42");
        shoe.setColor("Black");
        shoe.setQuantity(50);
        shoe.setPrice(new BigDecimal("120.00"));
        shoe.setBrand(brand);
        shoe.setCategories(Arrays.asList(category));
    }

    @Test
    @WithMockUser
    void testGetAllShoes() throws Exception {
        List<Shoe> shoes = Arrays.asList(shoe);
        when(shoeService.getAllShoes()).thenReturn(shoes);

        mockMvc.perform(get("/api/shoes")
)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].model").value("Air Max"));

        verify(shoeService).getAllShoes();
    }

    @Test
    @WithMockUser
    void testGetShoeById() throws Exception {
        when(shoeService.getShoeById(1L)).thenReturn(Optional.of(shoe));

        mockMvc.perform(get("/api/shoes/1")
)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.model").value("Air Max"));

        verify(shoeService).getShoeById(1L);
    }

    @Test
    @WithMockUser
    void testGetShoesByBrand() throws Exception {
        List<Shoe> shoes = Arrays.asList(shoe);
        when(shoeService.getShoesByBrand(1L)).thenReturn(shoes);

        mockMvc.perform(get("/api/shoes/brand/1")
)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].model").value("Air Max"));

        verify(shoeService).getShoesByBrand(1L);
    }

    @Test
    @WithMockUser
    void testGetShoesByCategory() throws Exception {
        List<Shoe> shoes = Arrays.asList(shoe);
        when(shoeService.getShoesByCategory(1L)).thenReturn(shoes);

        mockMvc.perform(get("/api/shoes/category/1")
)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].model").value("Air Max"));

        verify(shoeService).getShoesByCategory(1L);
    }

    @Test
    @WithMockUser
    void testGetLowStockShoes() throws Exception {
        List<Shoe> shoes = Arrays.asList(shoe);
        when(shoeService.getLowStockShoes(10)).thenReturn(shoes);

        mockMvc.perform(get("/api/shoes/low-stock/10")
)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].model").value("Air Max"));

        verify(shoeService).getLowStockShoes(10);
    }

    @Test
    @WithMockUser
    void testCreateShoe() throws Exception {
        when(shoeService.createShoe(any(Shoe.class))).thenReturn(shoe);

        mockMvc.perform(post("/api/shoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(shoe)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.model").value("Air Max"));

        verify(shoeService).createShoe(any(Shoe.class));
    }

    @Test
    @WithMockUser
    void testUpdateShoe() throws Exception {
        when(shoeService.updateShoe(eq(1L), any(Shoe.class))).thenReturn(shoe);

        mockMvc.perform(put("/api/shoes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(shoe)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.model").value("Air Max"));

        verify(shoeService).updateShoe(eq(1L), any(Shoe.class));
    }

    @Test
    @WithMockUser
    void testDeleteShoe() throws Exception {
        doNothing().when(shoeService).deleteShoe(1L);

        mockMvc.perform(delete("/api/shoes/1")
)
                .andExpect(status().isNoContent());

        verify(shoeService).deleteShoe(1L);
    }
}
