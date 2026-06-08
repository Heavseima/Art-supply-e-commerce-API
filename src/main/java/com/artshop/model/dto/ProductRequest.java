package com.artshop.model.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

/**
 * Immutable request body for creating or fully replacing a product.
 * The {@code id} is server-assigned on create and taken from the path on update.
 */
public record ProductRequest(
        @NotBlank(message = "categoryId is required")
        String categoryId,

        @NotBlank(message = "slug is required")
        @Size(max = 120, message = "slug must not exceed 120 characters")
        String slug,

        @NotBlank(message = "name is required")
        @Size(max = 200, message = "name must not exceed 200 characters")
        String name,

        String description,

        @NotNull(message = "price is required")
        @DecimalMin(value = "0.0", message = "price must not be negative")
        BigDecimal price,

        @NotNull(message = "stock is required")
        @Min(value = 0, message = "stock must not be negative")
        Integer stock,

        @Size(max = 2048, message = "imageUrl must not exceed 2048 characters")
        String imageUrl
) {
}
