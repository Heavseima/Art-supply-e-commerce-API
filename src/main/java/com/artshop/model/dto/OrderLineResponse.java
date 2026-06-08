package com.artshop.model.dto;

import java.math.BigDecimal;

/**
 * Immutable per-line confirmation returned inside an {@link OrderResponse}.
 * Deliberately narrow: exposes only what a shopper needs to see and never
 * leaks internal inventory state such as {@code stock}.
 */
public record OrderLineResponse(
        String productId,
        String name,
        String slug,
        BigDecimal price,
        int orderedQuantity,
        BigDecimal lineTotal
) {
}
