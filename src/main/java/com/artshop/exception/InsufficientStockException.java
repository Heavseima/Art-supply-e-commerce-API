package com.artshop.exception;

/**
 * Thrown when an order/cart line requests more units than are in stock.
 * Mapped to HTTP 409 (Conflict) by {@link GlobalExceptionHandler}.
 */
public class InsufficientStockException extends RuntimeException {

    public InsufficientStockException(String productId, int requested, int available) {
        super("Insufficient stock for product %s: requested %d, available %d"
                .formatted(productId, requested, available));
    }
}
