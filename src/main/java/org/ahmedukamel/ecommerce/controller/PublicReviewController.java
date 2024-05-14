package org.ahmedukamel.ecommerce.controller;

import lombok.RequiredArgsConstructor;
import org.ahmedukamel.ecommerce.dto.response.ApiResponse;
import org.ahmedukamel.ecommerce.service.ReviewService;
import org.ahmedukamel.ecommerce.util.ResponseUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "api/v1/public/review")
public class PublicReviewController {
    private final ReviewService service;

    @GetMapping(value = "all")
    public ResponseEntity<ApiResponse> getAllReviews() {
        ApiResponse response = service.getAllReviews();
        return ResponseUtils.acceptedResponse(response);
    }

    @GetMapping(value = "{reviewId}")
    public ResponseEntity<ApiResponse> getReview(@PathVariable(value = "reviewId") Integer reviewId) {
        ApiResponse response = service.getReview(reviewId);
        return ResponseUtils.acceptedResponse(response);
    }

    @GetMapping(value = "product/{productId}")
    public ResponseEntity<ApiResponse> getProductReviews(@PathVariable(value = "productId") Integer productId) {
        ApiResponse response = service.getProductReviews(productId);
        return ResponseUtils.acceptedResponse(response);
    }
}
