package se.lexicon.ecommerceworkshop.service;

import java.math.BigDecimal;
import java.util.List;
import org.example.entity.Product;
import se.lexicon.ecommerceworkshop.dto.PromotionResponse;

public interface PromotionService {

    List<PromotionResponse> getActivePromotions();

    BigDecimal calculateDiscount(Product product);
}
