package se.lexicon.ecommerceworkshop.service;

import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import se.lexicon.ecommerceworkshop.dto.CustomerRequest;
import se.lexicon.ecommerceworkshop.dto.CustomerResponse;

@Validated
public interface CustomerService {

    CustomerResponse register(@Valid CustomerRequest request);

    CustomerResponse findById(Long id);

    CustomerResponse update(Long id, @Valid CustomerRequest request);
}
