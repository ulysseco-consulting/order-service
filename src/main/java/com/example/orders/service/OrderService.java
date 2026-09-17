package com.example.orders.service;

import com.example.orders.api.OrderDetailDto;
import com.example.orders.api.OrderNotFoundException;
import com.example.orders.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public OrderDetailDto getOrder(Long orderId) {
        return orderRepository.findWithLineItemsById(orderId)
                .map(OrderDetailDto::from)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
    }
}
