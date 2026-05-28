package org.example.config;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.example.entity.Category;
import org.example.entity.Product;
import org.example.repository.CategoryRepository;
import org.example.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DataSeeder implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public DataSeeder(CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        Map<String, Category> categoriesByName = seedCategories();
        seedProducts(categoriesByName);
    }

    private Map<String, Category> seedCategories() {
        Map<String, Category> categoriesByName = new LinkedHashMap<>();

        for (String name : List.of("Electronics", "Books", "Home", "Clothing")) {
            Category category = categoryRepository.findByNameIgnoreCase(name)
                    .orElseGet(() -> {
                        Category newCategory = new Category();
                        newCategory.setName(name);
                        return categoryRepository.save(newCategory);
                    });
            categoriesByName.put(name, category);
        }

        return categoriesByName;
    }

    private void seedProducts(Map<String, Category> categoriesByName) {
        List<SeedProduct> products = List.of(
                new SeedProduct(
                        "Wireless Headphones",
                        "Electronics",
                        new BigDecimal("129.99"),
                        List.of("https://example.com/images/wireless-headphones.jpg")
                ),
                new SeedProduct(
                        "Java Persistence Handbook",
                        "Books",
                        new BigDecimal("39.99"),
                        List.of("https://example.com/images/jpa-handbook.jpg")
                ),
                new SeedProduct(
                        "Ceramic Coffee Mug",
                        "Home",
                        new BigDecimal("14.99"),
                        List.of("https://example.com/images/ceramic-mug.jpg")
                ),
                new SeedProduct(
                        "Cotton Hoodie",
                        "Clothing",
                        new BigDecimal("59.99"),
                        List.of("https://example.com/images/cotton-hoodie.jpg")
                )
        );

        for (SeedProduct seedProduct : products) {
            if (productRepository.existsByNameIgnoreCase(seedProduct.name())) {
                continue;
            }

            Category category = categoriesByName.get(seedProduct.categoryName());
            if (category == null) {
                throw new IllegalStateException("Missing seeded category: " + seedProduct.categoryName());
            }

            Product product = new Product();
            product.setName(seedProduct.name());
            product.setPrice(seedProduct.price());
            product.setCategory(category);
            product.getImageUrls().addAll(seedProduct.imageUrls());

            productRepository.save(product);
        }
    }

    private record SeedProduct(String name, String categoryName, BigDecimal price, List<String> imageUrls) {
    }
}
