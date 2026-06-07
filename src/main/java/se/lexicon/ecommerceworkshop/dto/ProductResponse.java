package se.lexicon.ecommerceworkshop.dto;

import java.math.BigDecimal;

public record ProductResponse(
        Long id,
        String name,
        BigDecimal price,
        String categoryName
) {
}
