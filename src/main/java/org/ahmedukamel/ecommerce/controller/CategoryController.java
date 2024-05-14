package org.ahmedukamel.ecommerce.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ahmedukamel.ecommerce.dto.response.ApiResponse;
import org.ahmedukamel.ecommerce.dto.CategoryDto;
import org.ahmedukamel.ecommerce.service.category.CategoryService;
import org.ahmedukamel.ecommerce.util.ResponseUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/v1/admin/category")
public class CategoryController {
    private final CategoryService service;

    public CategoryController(@Qualifier(value = "categoryServiceV1") CategoryService service) {
        this.service = service;
    }

    @PostMapping(value = "new")
    public ResponseEntity<ApiResponse> addCategory(@Valid @RequestBody CategoryDto request) {
        ApiResponse response = service.addCategory(request);
        return ResponseUtils.acceptedResponse(response);
    }

    @PutMapping(value = "update/{categoryId}")
    public ResponseEntity<ApiResponse> updateCategory(@PathVariable(value = "categoryId") Integer productId, @RequestBody CategoryDto request) {
        ApiResponse response = service.updateCategory(request, productId);
        return ResponseUtils.acceptedResponse(response);
    }

    @DeleteMapping(value = "delete/{categoryId}")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable(value = "categoryId") Integer categoryId) {
        ApiResponse response = service.deleteCategory(categoryId);
        return ResponseUtils.acceptedResponse(response);
    }
}
