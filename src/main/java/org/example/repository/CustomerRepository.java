package org.example.repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.example.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByEmail(String email);

    List<Customer> findByLastNameIgnoreCase(String lastName);

    List<Customer> findByAddressCityIgnoreCase(String city);

    List<Customer> findByEmailContainingIgnoreCase(String keyword);

    List<Customer> findByCreatedAtAfter(Instant createdAt);

    List<Customer> findByCreatedAtBetween(Instant start, Instant end);

    long countByAddressCityIgnoreCase(String city);

    boolean existsByEmail(String email);
}
