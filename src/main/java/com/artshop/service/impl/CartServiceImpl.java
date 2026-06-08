package com.artshop.service.impl;

import com.artshop.exception.ResourceNotFoundException;
import com.artshop.model.dto.CartValidationRequest;
import com.artshop.model.dto.CartValidationResponse;
import com.artshop.model.dto.OrderLine;
import com.artshop.model.entity.Product;
import com.artshop.repository.ProductMapper;
import com.artshop.service.CartService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    private final ProductMapper productMapper;

    public CartServiceImpl(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public CartValidationResponse validate(CartValidationRequest request) {
        List<String> ids = request.items().stream().map(OrderLine::productId).distinct().toList();
        Map<String, Product> products = productMapper.findByIds(ids).stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));

        boolean allValid = true;
        // estimatedTotal reflects every requested line (the cost the cart would
        // incur if all lines were fulfilled), not just the in-stock subset, so
        // the figure is never a silently-misleading partial sum.
        BigDecimal estimatedTotal = BigDecimal.ZERO;
        List<CartValidationResponse.LineResult> lines = new ArrayList<>();

        for (OrderLine line : request.items()) {
            Product product = products.get(line.productId());
            if (product == null) {
                throw new ResourceNotFoundException("Product", line.productId());
            }
            boolean inStock = product.getStock() >= line.quantity();
            BigDecimal lineTotal = product.getPrice().multiply(BigDecimal.valueOf(line.quantity()));

            estimatedTotal = estimatedTotal.add(lineTotal);
            if (!inStock) {
                allValid = false;
            }

            lines.add(new CartValidationResponse.LineResult(
                    product.getId(),
                    product.getName(),
                    line.quantity(),
                    product.getStock(),
                    inStock,
                    lineTotal));
        }

        return new CartValidationResponse(allValid, estimatedTotal, lines);
    }
}
