package se.lexicon.ecommerceworkshop.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.example.entity.Customer;
import org.example.entity.Order;
import org.example.entity.Product;
import org.example.repository.CustomerRepository;
import org.example.repository.OrderRepository;
import org.example.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.lexicon.ecommerceworkshop.dto.OrderItemRequest;
import se.lexicon.ecommerceworkshop.dto.OrderRequest;
import se.lexicon.ecommerceworkshop.dto.OrderResponse;
import se.lexicon.ecommerceworkshop.exception.BusinessRuleException;
import se.lexicon.ecommerceworkshop.exception.ResourceNotFoundException;
import se.lexicon.ecommerceworkshop.mapper.OrderMapper;
import se.lexicon.ecommerceworkshop.service.OrderService;
import se.lexicon.ecommerceworkshop.service.PromotionService;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final OrderMapper orderMapper;
    private final PromotionService promotionService;

    public OrderServiceImpl(
            OrderRepository orderRepository,
            CustomerRepository customerRepository,
            ProductRepository productRepository,
            OrderMapper orderMapper,
            PromotionService promotionService
    ) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
        this.orderMapper = orderMapper;
        this.promotionService = promotionService;
    }

    @Override
    @Transactional
    public OrderResponse placeOrder(OrderRequest request) {
        Customer customer = customerRepository.findById(request.customerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer", request.customerId()));

        List<OrderMapper.ResolvedOrderItem> resolvedItems = new ArrayList<>();
        for (OrderItemRequest itemRequest : request.items()) {
            Product product = productRepository.findById(itemRequest.productId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product", itemRequest.productId()));

            BigDecimal currentPrice = product.getPrice();
            if (currentPrice == null) {
                throw new BusinessRuleException("Product has no price: " + product.getId());
            }
            BigDecimal discount = promotionService.calculateDiscount(product);
            BigDecimal priceAtPurchase = currentPrice.subtract(discount).max(BigDecimal.ZERO);

            resolvedItems.add(new OrderMapper.ResolvedOrderItem(
                    product,
                    itemRequest.quantity(),
                    priceAtPurchase
            ));
        }

        Order order = orderMapper.toEntity(request, customer, resolvedItems);
        Order savedOrder = orderRepository.save(order);
        return orderMapper.toResponse(savedOrder);
    }
}
