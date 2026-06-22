package com.artshop.model.dto;

/**
 * Immutable API contract representing a product category.
 * Mirrors the frontend {@code Category} type.
 */
public record CategoryResponse(
        String id,
        String slug,
        String name,
        String tagline
) {
}
//