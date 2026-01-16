package com.shoewarehouse.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shoewarehouse.entity.Supplier;
import com.shoewarehouse.service.SupplierService;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SupplierController.class)
class SupplierControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SupplierService supplierService;

    @Autowired
    private ObjectMapper objectMapper;

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
    @WithMockUser
    void testGetAllSuppliers() throws Exception {
        List<Supplier> suppliers = Arrays.asList(supplier);
        when(supplierService.getAllSuppliers()).thenReturn(suppliers);

        mockMvc.perform(get("/api/suppliers")
)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("ABC Suppliers"));

        verify(supplierService).getAllSuppliers();
    }

    @Test
    @WithMockUser
    void testGetSupplierById() throws Exception {
        when(supplierService.getSupplierById(1L)).thenReturn(Optional.of(supplier));

        mockMvc.perform(get("/api/suppliers/1")
)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("ABC Suppliers"));

        verify(supplierService).getSupplierById(1L);
    }

    @Test
    @WithMockUser
    void testCreateSupplier() throws Exception {
        when(supplierService.createSupplier(any(Supplier.class))).thenReturn(supplier);

        mockMvc.perform(post("/api/suppliers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(supplier)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("ABC Suppliers"));

        verify(supplierService).createSupplier(any(Supplier.class));
    }

    @Test
    @WithMockUser
    void testUpdateSupplier() throws Exception {
        when(supplierService.updateSupplier(eq(1L), any(Supplier.class))).thenReturn(supplier);

        mockMvc.perform(put("/api/suppliers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(supplier)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("ABC Suppliers"));

        verify(supplierService).updateSupplier(eq(1L), any(Supplier.class));
    }

    @Test
    @WithMockUser
    void testDeleteSupplier() throws Exception {
        doNothing().when(supplierService).deleteSupplier(1L);

        mockMvc.perform(delete("/api/suppliers/1")
)
                .andExpect(status().isNoContent());

        verify(supplierService).deleteSupplier(1L);
    }
}
