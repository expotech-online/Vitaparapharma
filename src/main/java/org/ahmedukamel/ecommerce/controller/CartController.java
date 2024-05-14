package org.ahmedukamel.ecommerce.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ahmedukamel.ecommerce.dto.request.CartRequest;
import org.ahmedukamel.ecommerce.dto.response.ApiResponse;
import org.ahmedukamel.ecommerce.service.CartService;
import org.ahmedukamel.ecommerce.util.ResponseUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "api/v1/user/cart")
public class CartController {
    private final CartService service;

    @GetMapping(value = "my")
    public ResponseEntity<ApiResponse> getCart() {
        ApiResponse response = service.getCart();
        return ResponseUtils.acceptedResponse(response);
    }

    @PutMapping(value = "update")
    public ResponseEntity<ApiResponse> updateCart(@Valid @RequestBody CartRequest request) {
        ApiResponse response = service.updateCart(request);
        return ResponseUtils.acceptedResponse(response);
    }

    @DeleteMapping(value = "remove/{productId}")
    public ResponseEntity<ApiResponse> removeFromCart(@PathVariable(value = "productId") Integer productId) {
        ApiResponse response = service.removeFromCart(productId);
        return ResponseUtils.acceptedResponse(response);
    }
}
