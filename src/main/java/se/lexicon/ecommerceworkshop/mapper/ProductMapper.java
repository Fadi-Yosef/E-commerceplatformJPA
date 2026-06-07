package se.lexicon.ecommerceworkshop.mapper;

import org.example.entity.Category;
import org.example.entity.Product;
import org.springframework.stereotype.Component;
import se.lexicon.ecommerceworkshop.dto.CategoryResponse;
import se.lexicon.ecommerceworkshop.dto.ProductRequest;
import se.lexicon.ecommerceworkshop.dto.ProductResponse;

@Component
public class ProductMapper {

    public ProductResponse toResponse(Product product) {
        if (product == null) {
            return null;
        }

        Category category = product.getCategory();
        String categoryName = category == null ? null : category.getName();

        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getPrice(),
                categoryName
        );
    }

    public Product toEntity(ProductRequest request) {
        if (request == null) {
            return null;
        }

        Product product = new Product();
        product.setName(trim(request.name()));
        product.setPrice(request.price());
        return product;
    }

    public CategoryResponse toCategoryResponse(Category category) {
        if (category == null) {
            return null;
        }

        return new CategoryResponse(category.getId(), category.getName());
    }

    private String trim(String value) {
        return value == null ? null : value.trim();
    }
}
