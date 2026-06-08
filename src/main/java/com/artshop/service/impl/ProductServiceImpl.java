package com.artshop.service.impl;

import com.artshop.exception.ResourceNotFoundException;
import com.artshop.model.dto.PagedResponse;
import com.artshop.model.dto.ProductRequest;
import com.artshop.model.dto.ProductResponse;
import com.artshop.model.entity.Product;
import com.artshop.repository.CategoryMapper;
import com.artshop.repository.ProductMapper;
import com.artshop.service.ProductService;
import com.artshop.service.support.ProductDtoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductMapper productMapper;
    private final CategoryMapper categoryMapper;
    private final ProductDtoMapper dtoMapper;

    public ProductServiceImpl(ProductMapper productMapper,
                              CategoryMapper categoryMapper,
                              ProductDtoMapper dtoMapper) {
        this.productMapper = productMapper;
        this.categoryMapper = categoryMapper;
        this.dtoMapper = dtoMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<ProductResponse> findAll(int page, int size) {
        int offset = page * size;
        long totalElements = productMapper.countAll();
        List<ProductResponse> content = productMapper.findPage(size, offset).stream()
                .map(dtoMapper::toProductResponse)
                .toList();
        return PagedResponse.of(content, page, size, totalElements);
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<ProductResponse> findByCategory(String categoryId, int page, int size) {
        requireCategory(categoryId);
        int offset = page * size;
        long totalElements = productMapper.countByCategoryId(categoryId);
        List<ProductResponse> content = productMapper.findPageByCategoryId(categoryId, size, offset).stream()
                .map(dtoMapper::toProductResponse)
                .toList();
        return PagedResponse.of(content, page, size, totalElements);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse findById(String id) {
        Product product = productMapper.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", id));
        return dtoMapper.toProductResponse(product);
    }

    @Override
    @Transactional
    public ProductResponse create(ProductRequest request) {
        requireCategory(request.categoryId());
        String id = "p-" + UUID.randomUUID().toString().substring(0, 8);
        Product product = dtoMapper.toEntity(id, request);
        productMapper.insert(product);
        log.info("Product created: id={}, slug={}", id, request.slug());
        return dtoMapper.toProductResponse(product);
    }

    @Override
    @Transactional
    public ProductResponse update(String id, ProductRequest request) {
        if (productMapper.findById(id).isEmpty()) {
            throw new ResourceNotFoundException("Product", id);
        }
        requireCategory(request.categoryId());
        Product product = dtoMapper.toEntity(id, request);
        productMapper.update(product);
        log.info("Product updated: id={}", id);
        return dtoMapper.toProductResponse(product);
    }

    @Override
    @Transactional
    public void delete(String id) {
        int affected = productMapper.deleteById(id);
        if (affected == 0) {
            throw new ResourceNotFoundException("Product", id);
        }
        log.info("Product deleted: id={}", id);
    }

    /** Ensures the referenced category exists before products are read or written against it. */
    private void requireCategory(String categoryId) {
        if (categoryMapper.findById(categoryId).isEmpty()) {
            throw new ResourceNotFoundException("Category", categoryId);
        }
    }
}
