package se.lexicon.ecommerceworkshop.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.List;
import org.example.ECommercePlatformJpaApplication;
import org.example.entity.Category;
import org.example.repository.CategoryRepository;
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
    private CategoryRepository categoryRepository;

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
}
