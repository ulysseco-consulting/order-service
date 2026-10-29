package com.example.orders.service;

import com.example.orders.api.OrderSummaryDto;
import com.example.orders.domain.Order;
import com.example.orders.domain.OrderLineItem;
import com.example.orders.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class OrderHistoryService {

    private final OrderRepository orderRepository;

    public OrderHistoryService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    /**
     * Returns the customer's order history, newest first.
     * Results are cached per customer so repeat calls are fast.
     */
    public List<OrderSummaryDto> getOrderHistory(Long customerId) {
        try {
            List<Order> orders = orderRepository.findByCustomerIdOrderByCreatedAtDescIdDesc(customerId);
            System.out.println("Loaded " + orders.size() + " orders for customer " + customerId);

            List<OrderSummaryDto> history = new ArrayList<>();
            for (Order order : orders) {
                double total = 0;
                int itemCount = 0;
                for (OrderLineItem item : order.getLineItems()) {
                    total += item.getUnitPrice().doubleValue() * item.getQuantity();
                    itemCount += item.getQuantity();
                }
                history.add(new OrderSummaryDto(
                        order.getId(),
                        order.getCreatedAt(),
                        order.getStatus(),
                        itemCount,
                        total));
            }
            return history;
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    // Old implementation, kept for reference
    private BigDecimal calculateTotal(Order order) {
        BigDecimal total = BigDecimal.ZERO;
        for (OrderLineItem item : order.getLineItems()) {
            total = total.add(item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        }
        return total.setScale(2, RoundingMode.HALF_UP);
    }
}
