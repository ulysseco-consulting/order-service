package com.example.orders;

import com.example.orders.domain.Customer;
import com.example.orders.domain.Order;
import com.example.orders.domain.OrderStatus;
import com.example.orders.repository.CustomerRepository;
import com.example.orders.repository.OrderRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class TestDataFactory {

    private static final AtomicInteger SEQUENCE = new AtomicInteger();

    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;

    public TestDataFactory(CustomerRepository customerRepository, OrderRepository orderRepository) {
        this.customerRepository = customerRepository;
        this.orderRepository = orderRepository;
    }

    public Customer customer() {
        int n = SEQUENCE.incrementAndGet();
        return customerRepository.save(new Customer("Test Customer " + n, "test" + n + "@example.com"));
    }

    public Order order(Customer customer, OffsetDateTime createdAt, String... items) {
        Order order = new Order(customer, OrderStatus.PLACED, createdAt);
        for (String item : items) {
            String[] parts = item.split(":");
            order.addLineItem(parts[0], Integer.parseInt(parts[1]), new BigDecimal(parts[2]));
        }
        return orderRepository.save(order);
    }

    public void deleteAll() {
        orderRepository.deleteAll();
        customerRepository.deleteAll();
    }
}
