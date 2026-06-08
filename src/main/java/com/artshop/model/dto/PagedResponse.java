package com.artshop.model.dto;

import java.util.List;

/**
 * Immutable pagination envelope returned by list endpoints.
 *
 * @param <T> element type of the page content
 */
public record PagedResponse<T>(
        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages
) {
    public static <T> PagedResponse<T> of(List<T> content, int page, int size, long totalElements) {
        int totalPages = (size == 0) ? 0 : (int) Math.ceil((double) totalElements / size);
        return new PagedResponse<>(content, page, size, totalElements, totalPages);
    }
}
