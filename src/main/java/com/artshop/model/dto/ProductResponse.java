package com.artshop.model.dto;

import java.math.BigDecimal;

/**
 * Immutable API contract representing a single purchasable product.
 * Mirrors the frontend {@code Product} type.
 */
public record ProductResponse(
        String id,
        String categoryId,
        String slug,
        String name,
        String description,
        BigDecimal price,
        Integer stock,
        String imageUrl
) {
}
