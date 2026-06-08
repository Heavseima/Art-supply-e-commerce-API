package com.artshop.model.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * Immutable request to place an order for a set of products.
 */
public record OrderRequest(
        @NotBlank(message = "customerName is required")
        @Size(max = 200, message = "customerName must not exceed 200 characters")
        String customerName,

        @NotBlank(message = "customerEmail is required")
        @Email(message = "customerEmail must be a valid email address")
        String customerEmail,

        @NotBlank(message = "shippingAddress is required")
        @Size(max = 500, message = "shippingAddress must not exceed 500 characters")
        String shippingAddress,

        @NotNull(message = "items is required")
        @NotEmpty(message = "items must contain at least one line")
        @Valid
        List<OrderLine> items
) {
}
