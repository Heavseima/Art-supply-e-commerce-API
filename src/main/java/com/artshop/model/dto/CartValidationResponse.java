package com.artshop.model.dto;

import java.math.BigDecimal;
import java.util.List;

/**
 * Immutable result of validating a cart. {@code valid} is true only when every
 * line references an existing product with sufficient stock.
 */
public record CartValidationResponse(
        boolean valid,
        BigDecimal estimatedTotal,
        List<LineResult> lines
) {
    /**
     * Per-line outcome of cart validation.
     */
    public record LineResult(
            String productId,
            String productName,
            int requestedQuantity,
            int availableStock,
            boolean inStock,
            BigDecimal lineTotal
    ) {
    }
}
//