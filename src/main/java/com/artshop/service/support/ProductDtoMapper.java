package com.artshop.service.support;

import com.artshop.model.dto.CategoryRequest;
import com.artshop.model.dto.CategoryResponse;
import com.artshop.model.dto.OrderLineResponse;
import com.artshop.model.dto.ProductRequest;
import com.artshop.model.dto.ProductResponse;
import com.artshop.model.entity.Category;
import com.artshop.model.entity.Product;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Isolated, reusable entity -> API DTO translation. Keeps mapping rules out
 * of business services so they can be audited and adjusted independently.
 */
@Component
public class ProductDtoMapper {

    public CategoryResponse toCategoryResponse(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getSlug(),
                category.getName(),
                category.getTagline());
    }

    /**
     * Builds a {@link Category} entity from a write request and an assigned id.
     */
    public Category toEntity(String id, CategoryRequest request) {
        return Category.builder()
                .id(id)
                .slug(request.slug())
                .name(request.name())
                .tagline(request.tagline())
                .build();
    }

    /**
     * Builds a {@link Product} entity from a write request and an assigned id.
     */
    public Product toEntity(String id, ProductRequest request) {
        return Product.builder()
                .id(id)
                .categoryId(request.categoryId())
                .slug(request.slug())
                .name(request.name())
                .description(request.description())
                .price(request.price())
                .stock(request.stock())
                .imageUrl(request.imageUrl())
                .build();
    }

    public ProductResponse toProductResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getCategoryId(),
                product.getSlug(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStock(),
                product.getImageUrl());
    }

    /**
     * Maps a purchased product line to a shopper-facing order line. Deliberately
     * omits internal inventory state ({@code stock}) and computes the line total
     * from the unit price and the ordered quantity.
     */
    public OrderLineResponse toOrderLineResponse(Product product, int orderedQuantity) {
        BigDecimal lineTotal = product.getPrice().multiply(BigDecimal.valueOf(orderedQuantity));
        return new OrderLineResponse(
                product.getId(),
                product.getName(),
                product.getSlug(),
                product.getPrice(),
                orderedQuantity,
                lineTotal);
    }
}
