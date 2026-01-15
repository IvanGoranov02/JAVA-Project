package com.shoewarehouse.controller;

import com.shoewarehouse.entity.Shoe;
import com.shoewarehouse.service.ShoeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shoes")
public class ShoeController {

    @Autowired
    private ShoeService shoeService;

    @GetMapping
    public ResponseEntity<List<Shoe>> getAllShoes() {
        return ResponseEntity.ok(shoeService.getAllShoes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Shoe> getShoeById(@PathVariable Long id) {
        return shoeService.getShoeById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/brand/{brandId}")
    public ResponseEntity<List<Shoe>> getShoesByBrand(@PathVariable Long brandId) {
        return ResponseEntity.ok(shoeService.getShoesByBrand(brandId));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<Shoe>> getShoesByCategory(@PathVariable Long categoryId) {
        return ResponseEntity.ok(shoeService.getShoesByCategory(categoryId));
    }

    @GetMapping("/low-stock/{threshold}")
    public ResponseEntity<List<Shoe>> getLowStockShoes(@PathVariable Integer threshold) {
        return ResponseEntity.ok(shoeService.getLowStockShoes(threshold));
    }

    @PostMapping
    public ResponseEntity<Shoe> createShoe(@RequestBody Shoe shoe) {
        try {
            Shoe createdShoe = shoeService.createShoe(shoe);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdShoe);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Shoe> updateShoe(@PathVariable Long id, @RequestBody Shoe shoe) {
        try {
            Shoe updatedShoe = shoeService.updateShoe(id, shoe);
            return ResponseEntity.ok(updatedShoe);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShoe(@PathVariable Long id) {
        try {
            shoeService.deleteShoe(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
