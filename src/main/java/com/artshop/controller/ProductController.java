package com.artshop.controller;

import com.artshop.model.dto.ApiResponse;
import com.artshop.model.dto.PagedResponse;
import com.artshop.model.dto.ProductRequest;
import com.artshop.model.dto.ProductResponse;
import com.artshop.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    /** Default page size when the client does not specify one. */
    private static final int DEFAULT_PAGE_SIZE = 20;
    /** Hard upper bound on page size to protect the database from large scans. */
    private static final int MAX_PAGE_SIZE = 100;

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PagedResponse<ProductResponse>>> listProducts(
            @RequestParam(name = "categoryId", required = false) String categoryId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "" + DEFAULT_PAGE_SIZE) int size) {

        int safePage = Math.max(page, 0);
        int safeSize = clampSize(size);

        PagedResponse<ProductResponse> products = (categoryId == null)
                ? productService.findAll(safePage, safeSize)
                : productService.findByCategory(categoryId, safePage, safeSize);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(products, "Products retrieved successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> getProduct(@PathVariable String id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(productService.findById(id), "Product retrieved successfully"));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponse>> createProduct(@Valid @RequestBody ProductRequest request) {
        ProductResponse created = productService.create(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.id())
                .toUri();
        return ResponseEntity.created(location)
                .body(ApiResponse.created(created, "Product created successfully"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> updateProduct(@PathVariable String id,
                                                         @Valid @RequestBody ProductRequest request) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(productService.update(id, request), "Product updated successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable String id) {
        productService.delete(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.message("Product deleted successfully"));
    }

    private int clampSize(int size) {
        if (size < 1) {
            return DEFAULT_PAGE_SIZE;
        }
        return Math.min(size, MAX_PAGE_SIZE);
    }
}
