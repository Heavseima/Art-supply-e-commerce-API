package com.artshop.service.impl;

import com.artshop.exception.ResourceNotFoundException;
import com.artshop.model.dto.CategoryRequest;
import com.artshop.model.dto.CategoryResponse;
import com.artshop.model.entity.Category;
import com.artshop.repository.CategoryMapper;
import com.artshop.service.CategoryService;
import com.artshop.service.support.ProductDtoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryMapper categoryMapper;
    private final ProductDtoMapper dtoMapper;

    public CategoryServiceImpl(CategoryMapper categoryMapper, ProductDtoMapper dtoMapper) {
        this.categoryMapper = categoryMapper;
        this.dtoMapper = dtoMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> findAll() {
        return categoryMapper.findAll().stream()
                .map(dtoMapper::toCategoryResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponse findById(String id) {
        return categoryMapper.findById(id)
                .map(dtoMapper::toCategoryResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Category", id));
    }

    @Override
    @Transactional
    public CategoryResponse create(CategoryRequest request) {
        String id = "cat-" + UUID.randomUUID().toString().substring(0, 8);
        Category category = dtoMapper.toEntity(id, request);
        categoryMapper.insert(category);
        log.info("Category created: id={}, slug={}", id, request.slug());
        return dtoMapper.toCategoryResponse(category);
    }

    @Override
    @Transactional
    public CategoryResponse update(String id, CategoryRequest request) {
        if (categoryMapper.findById(id).isEmpty()) {
            throw new ResourceNotFoundException("Category", id);
        }
        Category category = dtoMapper.toEntity(id, request);
        categoryMapper.update(category);
        log.info("Category updated: id={}", id);
        return dtoMapper.toCategoryResponse(category);
    }

    @Override
    @Transactional
    public void delete(String id) {
        int affected = categoryMapper.deleteById(id);
        if (affected == 0) {
            throw new ResourceNotFoundException("Category", id);
        }
        log.info("Category deleted: id={}", id);
    }
}
