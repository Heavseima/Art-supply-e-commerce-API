package com.artshop.model.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

/**
 * Immutable confirmation returned after an order is accepted.
 */
public record OrderResponse(
        String orderReference,
        String customerName,
        String customerEmail,
        BigDecimal total,
        OffsetDateTime placedAt,
        List<OrderLineResponse> items
) {
}
//