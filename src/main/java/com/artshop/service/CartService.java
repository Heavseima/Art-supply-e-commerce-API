package com.artshop.service;

import com.artshop.model.dto.CartValidationRequest;
import com.artshop.model.dto.CartValidationResponse;

/**
 * Business operations for cart validation prior to checkout.
 */
public interface CartService {

    CartValidationResponse validate(CartValidationRequest request);
}
