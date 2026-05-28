package org.example.repository;

import java.time.Instant;
import java.util.List;
import org.example.entity.Order;
import org.example.entity.OrderStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByCustomer_Id(Long customerId);

    @EntityGraph(attributePaths = "items")
    List<Order> findByStatus(OrderStatus status);

    List<Order> findByOrderDateAfter(Instant orderDate);

    List<Order> findByOrderDateBetween(Instant start, Instant end);

    List<Order> findDistinctByItems_Product_Id(Long productId);

    long countByStatus(OrderStatus status);

    List<Order> findByCustomer_IdAndStatus(Long customerId, OrderStatus status);
}
