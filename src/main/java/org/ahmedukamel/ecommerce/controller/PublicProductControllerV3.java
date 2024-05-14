package org.ahmedukamel.ecommerce.controller;

import org.ahmedukamel.ecommerce.dto.response.ApiResponse;
import org.ahmedukamel.ecommerce.service.product.ProductService;
import org.ahmedukamel.ecommerce.util.ResponseUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api/v3/public/product")
public class PublicProductControllerV3 {
    private final ProductService service;

    public PublicProductControllerV3(@Qualifier("productServiceImplV3") ProductService service) {
        this.service = service;
    }

    @GetMapping(value = "all")
    public ResponseEntity<ApiResponse> getActiveProducts() {
        ApiResponse response = service.getActiveProducts();
        return ResponseUtils.acceptedResponse(response);
    }

    @GetMapping(value = "{productId}")
    public ResponseEntity<ApiResponse> getProduct(@PathVariable(value = "productId") Integer productId) {
        ApiResponse response = service.getProduct(productId);
        return ResponseUtils.acceptedResponse(response);
    }

    @GetMapping(value = "category/{categoryId}")
    public ResponseEntity<ApiResponse> getCategoryProducts(@PathVariable(value = "categoryId") Integer categoryId) {
        ApiResponse response = service.getCategoryProducts(categoryId);
        return ResponseUtils.acceptedResponse(response);
    }

    @GetMapping(value = "main/category/{mainCategoryId}")
    public ResponseEntity<ApiResponse> getMainCategoryProducts(@PathVariable(value = "mainCategoryId") Integer mainCategoryId) {
        ApiResponse response = service.getMainCategoryProducts(mainCategoryId);
        return ResponseUtils.acceptedResponse(response);
    }
}