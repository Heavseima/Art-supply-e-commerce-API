package com.artshop.service.impl;

import com.artshop.exception.InsufficientStockException;
import com.artshop.exception.ResourceNotFoundException;
import com.artshop.model.dto.OrderLine;
import com.artshop.model.dto.OrderLineResponse;
import com.artshop.model.dto.OrderRequest;
import com.artshop.model.dto.OrderResponse;
import com.artshop.model.entity.Product;
import com.artshop.repository.ProductMapper;
import com.artshop.service.OrderService;
import com.artshop.service.support.ProductDtoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    private final ProductMapper productMapper;
    private final ProductDtoMapper dtoMapper;

    public OrderServiceImpl(ProductMapper productMapper,
                            ProductDtoMapper dtoMapper) {
        this.productMapper = productMapper;
        this.dtoMapper = dtoMapper;
    }

    @Override
    @Transactional
    public OrderResponse placeOrder(OrderRequest request) {
        List<String> ids = request.items().stream().map(OrderLine::productId).distinct().toList();
        Map<String, Product> products = productMapper.findByIds(ids).stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));

        BigDecimal total = BigDecimal.ZERO;
        List<OrderLineResponse> items = new ArrayList<>();

        for (OrderLine line : request.items()) {
            Product product = products.get(line.productId());
            if (product == null) {
                throw new ResourceNotFoundException("Product", line.productId());
            }
            if (product.getStock() < line.quantity()) {
                log.warn("Insufficient stock placing order: productId={}, requested={}, available={}",
                        product.getId(), line.quantity(), product.getStock());
                throw new InsufficientStockException(
                        product.getId(), line.quantity(), product.getStock());
            }
            OrderLineResponse lineResponse = dtoMapper.toOrderLineResponse(product, line.quantity());
            total = total.add(lineResponse.lineTotal());
            items.add(lineResponse);
        }

        String orderReference = "ORD-" + UUID.randomUUID().toString().toUpperCase();
        log.info("Order placed: reference={}, lineCount={}, total={}",
                orderReference, items.size(), total);

        return new OrderResponse(
                orderReference,
                request.customerName(),
                request.customerEmail(),
                total,
                OffsetDateTime.now(),
                items);
    }
}
