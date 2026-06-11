package com.artshop.controller;

import com.artshop.model.dto.ApiResponse;
import com.artshop.model.dto.CartValidationRequest;
import com.artshop.model.dto.CartValidationResponse;
import com.artshop.service.CartService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/validate")
    public ResponseEntity<ApiResponse<CartValidationResponse>> validateCart(
            @Valid @RequestBody CartValidationRequest request) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(cartService.validate(request), "Cart validated successfully"));
    }
}
