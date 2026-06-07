package se.lexicon.ecommerceworkshop.service;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import se.lexicon.ecommerceworkshop.dto.ProductRequest;
import se.lexicon.ecommerceworkshop.dto.ProductResponse;

@Validated
public interface ProductService {

    ProductResponse create(@Valid ProductRequest request);

    List<ProductResponse> findAll();

    List<ProductResponse> searchByName(String name);
}
