package com.artshop.service;

import com.artshop.model.dto.OrderRequest;
import com.artshop.model.dto.OrderResponse;

/**
 * Business operations for placing orders.
 */
public interface OrderService {

    OrderResponse placeOrder(OrderRequest request);
}
