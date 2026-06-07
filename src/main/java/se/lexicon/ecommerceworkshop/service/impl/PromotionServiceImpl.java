package se.lexicon.ecommerceworkshop.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;
import org.example.entity.Product;
import org.example.entity.Promotion;
import org.example.repository.PromotionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.lexicon.ecommerceworkshop.dto.PromotionResponse;
import se.lexicon.ecommerceworkshop.mapper.PromotionMapper;
import se.lexicon.ecommerceworkshop.service.PromotionService;

@Service
public class PromotionServiceImpl implements PromotionService {

    private static final BigDecimal HUNDRED = BigDecimal.valueOf(100);

    private final PromotionRepository promotionRepository;
    private final PromotionMapper promotionMapper;

    public PromotionServiceImpl(PromotionRepository promotionRepository, PromotionMapper promotionMapper) {
        this.promotionRepository = promotionRepository;
        this.promotionMapper = promotionMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PromotionResponse> getActivePromotions() {
        return promotionRepository.findActiveToday().stream()
                .map(promotionMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal calculateDiscount(Product product) {
        if (product == null || product.getId() == null || product.getPrice() == null) {
            return BigDecimal.ZERO;
        }

        BigDecimal bestPercentage = promotionRepository.findActiveToday().stream()
                .filter(promotion -> appliesToProduct(promotion, product.getId()))
                .map(Promotion::getDiscountPercentage)
                .filter(Objects::nonNull)
                .max(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);

        return product.getPrice()
                .multiply(bestPercentage)
                .divide(HUNDRED, 2, RoundingMode.HALF_UP);
    }

    private boolean appliesToProduct(Promotion promotion, Long productId) {
        return promotion.getProducts().stream()
                .anyMatch(product -> productId.equals(product.getId()));
    }
}
