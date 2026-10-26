package com.example.orders.repository;

import com.example.orders.domain.Order;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * Loads an order together with its line items in a single query.
     */
    @EntityGraph(attributePaths = "lineItems")
    Optional<Order> findWithLineItemsById(Long id);

    /**
     * All orders for a customer, newest first.
     */
    List<Order> findByCustomerIdOrderByCreatedAtDescIdDesc(Long customerId);
}
