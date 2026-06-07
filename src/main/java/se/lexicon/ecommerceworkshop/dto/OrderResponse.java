package se.lexicon.ecommerceworkshop.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record OrderResponse(
        Long id,
        Long customerId,
        String customerEmail,
        Instant orderDate,
        String status,
        List<OrderItemResponse> items,
        BigDecimal total
) {

    public OrderResponse {
        items = List.copyOf(items);
    }
}
