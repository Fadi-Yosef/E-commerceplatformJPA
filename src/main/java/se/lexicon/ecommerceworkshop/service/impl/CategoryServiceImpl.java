package se.lexicon.ecommerceworkshop.service.impl;

import java.util.List;
import org.example.entity.Category;
import org.example.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.lexicon.ecommerceworkshop.dto.CategoryResponse;
import se.lexicon.ecommerceworkshop.exception.DuplicateResourceException;
import se.lexicon.ecommerceworkshop.mapper.CategoryMapper;
import se.lexicon.ecommerceworkshop.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Override
    @Transactional
    public CategoryResponse create(String name) {
        String normalizedName = normalize(name);
        if (categoryRepository.existsByNameIgnoreCase(normalizedName)) {
            throw new DuplicateResourceException("Category already exists: " + normalizedName);
        }

        Category category = new Category();
        category.setName(normalizedName);
        return categoryMapper.toResponse(categoryRepository.save(category));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> findAll() {
        return categoryRepository.findAll().stream()
                .map(categoryMapper::toResponse)
                .toList();
    }

    private String normalize(String name) {
        return name == null ? null : name.trim();
    }
}
