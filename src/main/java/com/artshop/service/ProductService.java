package com.artshop.service;

import com.artshop.model.dto.PagedResponse;
import com.artshop.model.dto.ProductRequest;
import com.artshop.model.dto.ProductResponse;

/**
 * Business operations for products.
 */
public interface ProductService {

    PagedResponse<ProductResponse> findAll(int page, int size);

    PagedResponse<ProductResponse> findByCategory(String categoryId, int page, int size);

    ProductResponse findById(String id);

    ProductResponse create(ProductRequest request);

    ProductResponse update(String id, ProductRequest request);

    void delete(String id);
}
