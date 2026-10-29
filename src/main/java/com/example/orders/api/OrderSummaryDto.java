package com.example.orders.api;

import com.example.orders.domain.OrderStatus;

import java.time.OffsetDateTime;

public record OrderSummaryDto(
        Long orderId,
        OffsetDateTime createdAt,
        OrderStatus status,
        int itemCount,
        double total) {
}
