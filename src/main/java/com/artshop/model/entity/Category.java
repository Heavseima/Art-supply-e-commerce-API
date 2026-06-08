package com.artshop.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Mutable database mapping for the {@code categories} table.
 * Never exposed directly through the API layer - map to a DTO first.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    private String id;
    private String slug;
    private String name;
    private String tagline;
}
