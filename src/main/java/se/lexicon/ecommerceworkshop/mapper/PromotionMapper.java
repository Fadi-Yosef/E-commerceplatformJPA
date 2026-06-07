package se.lexicon.ecommerceworkshop.mapper;

import org.example.entity.Promotion;
import org.springframework.stereotype.Component;
import se.lexicon.ecommerceworkshop.dto.PromotionResponse;

@Component
public class PromotionMapper {

    public PromotionResponse toResponse(Promotion promotion) {
        if (promotion == null) {
            return null;
        }

        return new PromotionResponse(
                promotion.getId(),
                promotion.getCode(),
                promotion.getStartDate(),
                promotion.getEndDate(),
                promotion.getDiscountPercentage()
        );
    }
}
