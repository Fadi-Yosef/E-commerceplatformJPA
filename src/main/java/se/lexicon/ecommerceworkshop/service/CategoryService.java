package se.lexicon.ecommerceworkshop.service;

import jakarta.validation.constraints.NotBlank;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import se.lexicon.ecommerceworkshop.dto.CategoryResponse;

@Validated
public interface CategoryService {

    CategoryResponse create(@NotBlank(message = "Category name is required") String name);

    List<CategoryResponse> findAll();
}
