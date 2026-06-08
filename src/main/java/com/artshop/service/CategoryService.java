package com.artshop.service;

import com.artshop.model.dto.CategoryRequest;
import com.artshop.model.dto.CategoryResponse;

import java.util.List;

/**
 * Business operations for product categories.
 */
public interface CategoryService {

    List<CategoryResponse> findAll();

    CategoryResponse findById(String id);

    CategoryResponse create(CategoryRequest request);

    CategoryResponse update(String id, CategoryRequest request);

    void delete(String id);
}
