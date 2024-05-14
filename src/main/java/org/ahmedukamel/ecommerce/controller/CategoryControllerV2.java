package org.ahmedukamel.ecommerce.controller;

import jakarta.validation.Valid;
import org.ahmedukamel.ecommerce.dto.CategoryDtoV2;
import org.ahmedukamel.ecommerce.dto.response.ApiResponse;
import org.ahmedukamel.ecommerce.service.category.CategoryService;
import org.ahmedukamel.ecommerce.util.ResponseUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/v2/admin/category")
public class CategoryControllerV2 {
    private final CategoryService service;

    public CategoryControllerV2(@Qualifier(value = "categoryServiceV2") CategoryService service) {
        this.service = service;
    }

    @PostMapping(value = "new")
    public ResponseEntity<ApiResponse> addCategory(@Valid @RequestBody CategoryDtoV2 request) {
        ApiResponse response = service.addCategory(request);
        return ResponseUtils.acceptedResponse(response);
    }

    @PutMapping(value = "update/{categoryId}")
    public ResponseEntity<ApiResponse> updateCategory(@Valid @RequestBody CategoryDtoV2 request, @PathVariable(value = "categoryId") Integer productId) {
        ApiResponse response = service.updateCategory(request, productId);
        return ResponseUtils.acceptedResponse(response);
    }
}
