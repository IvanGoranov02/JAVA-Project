package com.shoewarehouse.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shoewarehouse.entity.Brand;
import com.shoewarehouse.service.BrandService;
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

@WebMvcTest(BrandController.class)
class BrandControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BrandService brandService;

    @Autowired
    private ObjectMapper objectMapper;

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
    @WithMockUser
    void testGetAllBrands() throws Exception {
        List<Brand> brands = Arrays.asList(brand);
        when(brandService.getAllBrands()).thenReturn(brands);

        mockMvc.perform(get("/api/brands"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Nike"));

        verify(brandService).getAllBrands();
    }

    @Test
    @WithMockUser
    void testGetBrandById() throws Exception {
        when(brandService.getBrandById(1L)).thenReturn(Optional.of(brand));

        mockMvc.perform(get("/api/brands/1")
)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Nike"));

        verify(brandService).getBrandById(1L);
    }

    @Test
    @WithMockUser
    void testCreateBrand() throws Exception {
        when(brandService.createBrand(any(Brand.class))).thenReturn(brand);

        mockMvc.perform(post("/api/brands")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(brand)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Nike"));

        verify(brandService).createBrand(any(Brand.class));
    }

    @Test
    @WithMockUser
    void testUpdateBrand() throws Exception {
        when(brandService.updateBrand(eq(1L), any(Brand.class))).thenReturn(brand);

        mockMvc.perform(put("/api/brands/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(brand)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Nike"));

        verify(brandService).updateBrand(eq(1L), any(Brand.class));
    }

    @Test
    @WithMockUser
    void testDeleteBrand() throws Exception {
        doNothing().when(brandService).deleteBrand(1L);

        mockMvc.perform(delete("/api/brands/1")
)
                .andExpect(status().isNoContent());

        verify(brandService).deleteBrand(1L);
    }
}
