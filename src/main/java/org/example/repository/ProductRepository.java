package org.example.repository;

import java.math.BigDecimal;
import java.util.List;
import org.example.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

    boolean existsByNameIgnoreCase(String name);

    List<Product> findByCategoryNameIgnoreCase(String categoryName);

    List<Product> findByPriceBetween(BigDecimal minimumPrice, BigDecimal maximumPrice);

    List<Product> findByNameContainingIgnoreCase(String keyword);

    List<Product> findByPriceLessThan(BigDecimal price);

    List<Product> findAllByOrderByPriceAsc();

    List<Product> findAllByOrderByPriceDesc();

    long countByCategoryNameIgnoreCase(String categoryName);

    List<Product> findByCategory_Id(Long categoryId);
}
