package se.lexicon.ecommerceworkshop.mapper;

import org.example.entity.Category;
import org.springframework.stereotype.Component;
import se.lexicon.ecommerceworkshop.dto.CategoryResponse;

@Component
public class CategoryMapper {

    public CategoryResponse toResponse(Category category) {
        if (category == null) {
            return null;
        }

        return new CategoryResponse(category.getId(), category.getName());
    }
}
