package se.lexicon.ecommerceworkshop.service;

import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import se.lexicon.ecommerceworkshop.dto.OrderRequest;
import se.lexicon.ecommerceworkshop.dto.OrderResponse;

@Validated
public interface OrderService {

    OrderResponse placeOrder(@Valid OrderRequest request);
}
