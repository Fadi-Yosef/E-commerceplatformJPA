package se.lexicon.ecommerceworkshop.mapper;

import java.math.BigDecimal;
import java.util.List;
import org.example.entity.Customer;
import org.example.entity.Order;
import org.example.entity.OrderItem;
import org.example.entity.OrderStatus;
import org.example.entity.Product;
import org.springframework.stereotype.Component;
import se.lexicon.ecommerceworkshop.dto.OrderItemResponse;
import se.lexicon.ecommerceworkshop.dto.OrderRequest;
import se.lexicon.ecommerceworkshop.dto.OrderResponse;

@Component
public class OrderMapper {

    public OrderResponse toResponse(Order order) {
        if (order == null) {
            return null;
        }

        List<OrderItemResponse> items = order.getItems().stream()
                .map(this::toItemResponse)
                .toList();
        BigDecimal total = items.stream()
                .map(OrderItemResponse::lineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Customer customer = order.getCustomer();

        return new OrderResponse(
                order.getId(),
                customer == null ? null : customer.getId(),
                customer == null ? null : customer.getEmail(),
                order.getOrderDate(),
                order.getStatus() == null ? null : order.getStatus().name(),
                items,
                total
        );
    }

    public Order toEntity(OrderRequest request) {
        Order order = new Order();
        order.setStatus(OrderStatus.CREATED);
        return order;
    }

    public Order toEntity(OrderRequest request, Customer customer, List<ResolvedOrderItem> resolvedItems) {
        Order order = toEntity(request);
        order.setCustomer(customer);
        resolvedItems.forEach(resolvedItem -> order.addItem(toOrderItem(resolvedItem)));
        return order;
    }

    private OrderItemResponse toItemResponse(OrderItem item) {
        Product product = item.getProduct();
        BigDecimal lineTotal = item.getPriceAtPurchase()
                .multiply(BigDecimal.valueOf(item.getQuantity()));

        return new OrderItemResponse(
                item.getId(),
                product == null ? null : product.getId(),
                product == null ? null : product.getName(),
                item.getQuantity(),
                item.getPriceAtPurchase(),
                lineTotal
        );
    }

    private OrderItem toOrderItem(ResolvedOrderItem resolvedItem) {
        OrderItem item = new OrderItem();
        item.setProduct(resolvedItem.product());
        item.setQuantity(resolvedItem.quantity());
        item.setPriceAtPurchase(resolvedItem.priceAtPurchase());
        return item;
    }

    public record ResolvedOrderItem(Product product, Integer quantity, BigDecimal priceAtPurchase) {
    }
}
