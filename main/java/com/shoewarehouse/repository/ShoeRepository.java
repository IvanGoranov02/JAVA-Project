package com.shoewarehouse.repository;

import com.shoewarehouse.entity.Shoe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShoeRepository extends JpaRepository<Shoe, Long> {
    List<Shoe> findByBrandId(Long brandId);
    List<Shoe> findByModelContainingIgnoreCase(String model);
    List<Shoe> findByQuantityLessThan(Integer quantity);
    
    @Query("SELECT s FROM Shoe s JOIN s.categories c WHERE c.id = :categoryId")
    List<Shoe> findByCategoryId(@Param("categoryId") Long categoryId);
}
