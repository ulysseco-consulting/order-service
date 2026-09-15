package com.example.orders.repository;

import com.example.orders.domain.Order;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * Loads an order together with its line items in a single query.
     */
    @EntityGraph(attributePaths = "lineItems")
    Optional<Order> findWithLineItemsById(Long id);
}
