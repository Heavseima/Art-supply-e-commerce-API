package com.artshop.model.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * Immutable request asking the backend to validate a cart's line items
 * (existence + stock availability) before checkout.
 */
public record CartValidationRequest(
        @NotNull(message = "items is required")
        @NotEmpty(message = "items must contain at least one line")
        @Valid
        List<OrderLine> items
) {
}
