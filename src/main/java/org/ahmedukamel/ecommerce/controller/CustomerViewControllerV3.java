package org.ahmedukamel.ecommerce.controller;

import org.ahmedukamel.ecommerce.dto.response.ApiResponse;
import org.ahmedukamel.ecommerce.service.CustomerViewService;
import org.ahmedukamel.ecommerce.util.ResponseUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api/v3/user")
public class CustomerViewControllerV3 {
    private final CustomerViewService service;

    public CustomerViewControllerV3(@Qualifier("customerViewServiceImplV3") CustomerViewService service) {
        this.service = service;
    }

    @GetMapping(value = "product/all")
    public ResponseEntity<ApiResponse> getAllProductsForCustomer() {
        ApiResponse response = service.getAllProductsForCustomer();
        return ResponseUtils.acceptedResponse(response);
    }

    @GetMapping(value = "product/{productId}")
    public ResponseEntity<ApiResponse> getProductForCustomer(@PathVariable(value = "productId") Integer productId) {
        ApiResponse response = service.getProductForCustomer(productId);
        return ResponseUtils.acceptedResponse(response);
    }

    @GetMapping(value = "product/category/{categoryId}")
    public ResponseEntity<ApiResponse> getCategoryProductsForCustomer(@PathVariable(value = "categoryId") Integer categoryId) {
        ApiResponse response = service.getCategoryProductsForCustomer(categoryId);
        return ResponseUtils.acceptedResponse(response);
    }

    @GetMapping(value = "product/main/category/{mainCategoryId}")
    public ResponseEntity<ApiResponse> getMainCategoryProductsForCustomer(@PathVariable(value = "mainCategoryId") Integer mainCategoryId) {
        ApiResponse response = service.getMainCategoryProductsForCustomer(mainCategoryId);
        return ResponseUtils.acceptedResponse(response);
    }
}
