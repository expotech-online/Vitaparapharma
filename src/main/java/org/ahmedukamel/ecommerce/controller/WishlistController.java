package org.ahmedukamel.ecommerce.controller;

import lombok.RequiredArgsConstructor;
import org.ahmedukamel.ecommerce.dto.response.ApiResponse;
import org.ahmedukamel.ecommerce.service.WishlistService;
import org.ahmedukamel.ecommerce.util.ResponseUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "api/v1/user/wishlist")
public class WishlistController {
    private final WishlistService service;

    @GetMapping(value = "my")
    public ResponseEntity<ApiResponse> getWishlist() {
        ApiResponse response = service.getWishlist();
        return ResponseUtils.acceptedResponse(response);
    }

    @PutMapping(value = "add/{productId}")
    public ResponseEntity<ApiResponse> addWishlistItem(@PathVariable(value = "productId") Integer productId) {
        ApiResponse response = service.addWishlistItem(productId);
        return ResponseUtils.acceptedResponse(response);
    }

    @DeleteMapping(value = "remove/{productId}")
    public ResponseEntity<ApiResponse> deleteWishlistItem(@PathVariable(value = "productId") Integer productId) {
        ApiResponse response = service.deleteWishlistItem(productId);
        return ResponseUtils.acceptedResponse(response);
    }
}
