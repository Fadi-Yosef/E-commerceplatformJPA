package se.lexicon.ecommerceworkshop.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.example.ECommercePlatformJpaApplication;
import org.example.entity.Category;
import org.example.entity.Product;
import org.example.entity.Promotion;
import org.example.repository.CategoryRepository;
import org.example.repository.ProductRepository;
import org.example.repository.PromotionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import se.lexicon.ecommerceworkshop.dto.CustomerRequest;
import se.lexicon.ecommerceworkshop.dto.CustomerResponse;
import se.lexicon.ecommerceworkshop.dto.OrderItemRequest;
import se.lexicon.ecommerceworkshop.dto.OrderRequest;
import se.lexicon.ecommerceworkshop.dto.OrderResponse;
import se.lexicon.ecommerceworkshop.dto.ProductRequest;
import se.lexicon.ecommerceworkshop.dto.ProductResponse;
import se.lexicon.ecommerceworkshop.dto.PromotionResponse;
import se.lexicon.ecommerceworkshop.exception.DuplicateResourceException;
import se.lexicon.ecommerceworkshop.exception.ResourceNotFoundException;

@SpringBootTest(classes = ECommercePlatformJpaApplication.class)
@Transactional
class ServiceLayerIntegrationTest {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private PromotionService promotionService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PromotionRepository promotionRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void registerFindAndUpdateCustomer() {
        CustomerResponse registered = customerService.register(customerRequest(
                "part3.customer@example.com",
                "Main Street 1"
        ));

        assertNotNull(registered.id());
        assertEquals("Ada Lovelace", registered.fullName());
        assertEquals("part3.customer@example.com", registered.email());

        CustomerResponse found = customerService.findById(registered.id());
        assertEquals(registered.id(), found.id());

        CustomerResponse updated = customerService.update(registered.id(), new CustomerRequest(
                "Ada",
                "Byron",
                "part3.customer.updated@example.com",
                "password123",
                "Second Street 2",
                "Stockholm",
                "22222"
        ));

        assertEquals("Ada Byron", updated.fullName());
        assertEquals("part3.customer.updated@example.com", updated.email());
        assertEquals("Second Street 2", updated.addressResponse().street());
    }

    @Test
    void registerRejectsDuplicateEmail() {
        customerService.register(customerRequest("part3.duplicate@example.com", "Main Street 1"));

        assertThrows(DuplicateResourceException.class, () ->
                customerService.register(customerRequest("PART3.DUPLICATE@EXAMPLE.COM", "Other Street 2"))
        );
    }

    @Test
    void createFindAndSearchProduct() {
        Category category = getCategory("Electronics");
        ProductResponse created = productService.create(new ProductRequest(
                "Workshop Action Camera",
                new BigDecimal("79.99"),
                category.getId()
        ));

        assertNotNull(created.id());
        assertEquals("Electronics", created.categoryName());

        assertTrue(productService.findAll().stream()
                .anyMatch(product -> product.id().equals(created.id())));
        assertTrue(productService.searchByName("action camera").stream()
                .anyMatch(product -> product.id().equals(created.id())));
    }

    @Test
    void createProductRejectsMissingCategory() {
        ProductRequest request = new ProductRequest(
                "Product Without Category",
                new BigDecimal("9.99"),
                999_999L
        );

        assertThrows(ResourceNotFoundException.class, () -> productService.create(request));
    }

    @Test
    void createAndFindCategories() {
        var created = categoryService.create("Workshop Games");

        assertNotNull(created.id());
        assertEquals("Workshop Games", created.name());
        assertTrue(categoryService.findAll().stream()
                .anyMatch(category -> category.id().equals(created.id())));
    }

    @Test
    void createCategoryRejectsDuplicateName() {
        categoryService.create("Workshop Duplicate Category");

        assertThrows(DuplicateResourceException.class, () ->
                categoryService.create(" workshop duplicate category ")
        );
    }

    @Test
    void promotionServiceFindsActivePromotionsAndCalculatesBestDiscount() {
        Category category = getCategory("Electronics");
        ProductResponse productResponse = productService.create(new ProductRequest(
                "Workshop Discount Speaker",
                new BigDecimal("100.00"),
                category.getId()
        ));
        Product product = productRepository.findById(productResponse.id()).orElseThrow();
        Promotion promotion = createActivePromotion("PART3-BEST-15", new BigDecimal("15.00"), product);

        List<PromotionResponse> activePromotions = promotionService.getActivePromotions();
        BigDecimal discount = promotionService.calculateDiscount(product);

        assertTrue(activePromotions.stream()
                .anyMatch(activePromotion -> activePromotion.id().equals(promotion.getId())));
        assertEquals(new BigDecimal("15.00"), discount);
    }

    @Test
    void placeOrderCapturesCurrentProductPrice() {
        CustomerResponse customer = customerService.register(customerRequest(
                "part3.order@example.com",
                "Order Street 1"
        ));
        Category category = getCategory("Books");
        ProductResponse product = productService.create(new ProductRequest(
                "Workshop Order Book",
                new BigDecimal("19.99"),
                category.getId()
        ));

        OrderResponse order = orderService.placeOrder(new OrderRequest(
                customer.id(),
                List.of(new OrderItemRequest(product.id(), 2))
        ));

        assertNotNull(order.id());
        assertEquals(customer.id(), order.customerId());
        assertEquals("CREATED", order.status());
        assertEquals(1, order.items().size());
        assertEquals(new BigDecimal("19.99"), order.items().getFirst().priceAtPurchase());
        assertEquals(new BigDecimal("39.98"), order.total());
    }

    @Test
    void placeOrderAppliesActivePromotionDiscount() {
        CustomerResponse customer = customerService.register(customerRequest(
                "part3.discount.order@example.com",
                "Discount Order Street 1"
        ));
        Category category = getCategory("Home");
        ProductResponse productResponse = productService.create(new ProductRequest(
                "Workshop Discount Mug",
                new BigDecimal("20.00"),
                category.getId()
        ));
        Product product = productRepository.findById(productResponse.id()).orElseThrow();
        createActivePromotion("PART3-ORDER-10", new BigDecimal("10.00"), product);

        OrderResponse order = orderService.placeOrder(new OrderRequest(
                customer.id(),
                List.of(new OrderItemRequest(productResponse.id(), 2))
        ));

        assertEquals(new BigDecimal("18.00"), order.items().getFirst().priceAtPurchase());
        assertEquals(new BigDecimal("36.00"), order.total());
    }

    private CustomerRequest customerRequest(String email, String street) {
        return new CustomerRequest(
                "Ada",
                "Lovelace",
                email,
                "password123",
                street,
                "Stockholm",
                "11111"
        );
    }

    private Category getCategory(String name) {
        return categoryRepository.findByNameIgnoreCase(name)
                .orElseThrow();
    }

    private Promotion createActivePromotion(String code, BigDecimal discountPercentage, Product product) {
        Promotion promotion = new Promotion();
        promotion.setCode(code);
        promotion.setStartDate(LocalDate.now().minusDays(1));
        promotion.setEndDate(LocalDate.now().plusDays(1));
        promotion.setDiscountPercentage(discountPercentage);
        promotion.addProduct(product);
        Promotion savedPromotion = promotionRepository.save(promotion);
        productRepository.save(product);
        entityManager.flush();
        entityManager.clear();
        return savedPromotion;
    }
}
