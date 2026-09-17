package com.example.orders.api;

import com.example.orders.domain.Order;
import com.example.orders.domain.OrderLineItem;
import com.example.orders.domain.OrderStatus;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

public record OrderDetailDto(
        Long orderId,
        OffsetDateTime createdAt,
        OrderStatus status,
        List<LineItemDto> lineItems,
        BigDecimal total) {

    public record LineItemDto(String productName, int quantity, BigDecimal unitPrice) {

        static LineItemDto from(OrderLineItem item) {
            return new LineItemDto(item.getProductName(), item.getQuantity(), item.getUnitPrice());
        }
    }

    public static OrderDetailDto from(Order order) {
        return new OrderDetailDto(
                order.getId(),
                order.getCreatedAt(),
                order.getStatus(),
                order.getLineItems().stream().map(LineItemDto::from).toList(),
                order.total());
    }
}
