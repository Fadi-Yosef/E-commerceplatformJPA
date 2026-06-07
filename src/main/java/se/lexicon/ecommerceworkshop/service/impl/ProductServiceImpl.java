package se.lexicon.ecommerceworkshop.service.impl;

import java.util.List;
import org.example.entity.Category;
import org.example.entity.Product;
import org.example.repository.CategoryRepository;
import org.example.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.lexicon.ecommerceworkshop.dto.ProductRequest;
import se.lexicon.ecommerceworkshop.dto.ProductResponse;
import se.lexicon.ecommerceworkshop.exception.ResourceNotFoundException;
import se.lexicon.ecommerceworkshop.mapper.ProductMapper;
import se.lexicon.ecommerceworkshop.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    public ProductServiceImpl(
            ProductRepository productRepository,
            CategoryRepository categoryRepository,
            ProductMapper productMapper
    ) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.productMapper = productMapper;
    }

    @Override
    @Transactional
    public ProductResponse create(ProductRequest request) {
        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category", request.categoryId()));

        Product product = productMapper.toEntity(request);
        product.setCategory(category);
        Product savedProduct = productRepository.save(product);
        return productMapper.toResponse(savedProduct);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> findAll() {
        return productRepository.findAll().stream()
                .map(productMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> searchByName(String name) {
        String keyword = name == null ? "" : name.trim();
        return productRepository.findByNameContainingIgnoreCase(keyword).stream()
                .map(productMapper::toResponse)
                .toList();
    }
}
