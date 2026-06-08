package com.artshop.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Immutable request body for creating or fully replacing a category.
 * The {@code id} is server-assigned on create and taken from the path on update.
 */
public record CategoryRequest(
        @NotBlank(message = "slug is required")
        @Size(max = 80, message = "slug must not exceed 80 characters")
        String slug,

        @NotBlank(message = "name is required")
        @Size(max = 120, message = "name must not exceed 120 characters")
        String name,

        @Size(max = 255, message = "tagline must not exceed 255 characters")
        String tagline
) {
}
