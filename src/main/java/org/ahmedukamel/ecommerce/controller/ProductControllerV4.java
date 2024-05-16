package org.ahmedukamel.ecommerce.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.ahmedukamel.ecommerce.dto.ProductDtoV4;
import org.ahmedukamel.ecommerce.dto.response.ApiResponse;
import org.ahmedukamel.ecommerce.model.enumeration.FilterType;
import org.ahmedukamel.ecommerce.service.product.IProductManagementService;
import org.ahmedukamel.ecommerce.service.product.ITempProductService;
import org.ahmedukamel.ecommerce.util.ResponseUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(value = "api/v4")
@Validated
public class ProductControllerV4 {
    final IProductManagementService managementService;
    final ITempProductService service;

    public ProductControllerV4(IProductManagementService adminService, ITempProductService service) {
        this.managementService = adminService;
        this.service = service;
    }

    @PreAuthorize(value = "hasAnyAuthority('CONTENT_CREATOR', 'ADMIN', 'SUPER_ADMIN')")
    @PostMapping(value = "custom/product")
    public ResponseEntity<ApiResponse> addProduct(@RequestParam(value = "product") @Valid ProductDtoV4 request,
                                                  @RequestParam(value = "images") MultipartFile[] files) {
        ApiResponse response = managementService.addProduct(request, files);
        return ResponseUtils.acceptedResponse(response);
    }

    @PreAuthorize(value = "hasAnyAuthority('CONTENT_CREATOR', 'ADMIN', 'SUPER_ADMIN')")
    @PutMapping(value = "custom/product/{productId}")
    public ResponseEntity<ApiResponse> updateProduct(@PathVariable(value = "productId") Integer productId, @Valid @RequestBody ProductDtoV4 request) {
        ApiResponse response = managementService.updateProduct(productId, request);
        return ResponseUtils.acceptedResponse(response);
    }

    @PreAuthorize(value = "hasAnyAuthority('CONTENT_CREATOR', 'ADMIN', 'SUPER_ADMIN')")
    @GetMapping(value = "custom/product/{productId}")
    public ResponseEntity<ApiResponse> updateProduct(@PathVariable(value = "productId") Integer productId) {
        ApiResponse response = managementService.getProduct(productId);
        return ResponseUtils.acceptedResponse(response);
    }

    @PreAuthorize(value = "hasAnyAuthority('CONTENT_CREATOR', 'ADMIN', 'SUPER_ADMIN')")
    @GetMapping(value = "custom/product")
    public ResponseEntity<ApiResponse> getProductsByActiveStatus(@RequestParam(value = "active") boolean active,
                                                                 @Min(value = 1) @RequestParam(value = "size", defaultValue = "10") long pageSize,
                                                                 @Min(value = 1) @RequestParam(value = "page", defaultValue = "1") long pageNumber) {
        ApiResponse response = managementService.getProductsByActiveStatus(active, pageSize, pageNumber);
        return ResponseUtils.acceptedResponse(response);
    }

    @PreAuthorize(value = "hasAnyAuthority('CONTENT_CREATOR', 'ADMIN', 'SUPER_ADMIN')")
    @GetMapping(value = "custom/product/all")
    public ResponseEntity<ApiResponse> getProducts(@Min(value = 1) @RequestParam(value = "size", defaultValue = "10") long pageSize,
                                                   @Min(value = 1) @RequestParam(value = "page", defaultValue = "1") long pageNumber) {
        ApiResponse response = managementService.getProducts(pageSize, pageNumber);
        return ResponseUtils.acceptedResponse(response);
    }

    @PreAuthorize(value = "hasAnyAuthority('CONTENT_CREATOR', 'ADMIN', 'SUPER_ADMIN')")
    @PutMapping(value = "custom/product/{productId}/status")
    public ResponseEntity<ApiResponse> setProductActiveStatus(@PathVariable(value = "productId") int productId, @RequestParam(value = "active") boolean active) {
        ApiResponse response = managementService.setProductActiveStatus(productId, active);
        return ResponseUtils.acceptedResponse(response);
    }

    @PreAuthorize(value = "hasAnyAuthority('CONTENT_CREATOR', 'ADMIN', 'SUPER_ADMIN')")
    @PutMapping(value = "custom/product/{productId}/upload")
    public ResponseEntity<ApiResponse> uploadImages(@PathVariable(value = "productId") int productId, @RequestParam(value = "images") MultipartFile[] images) {
        ApiResponse response = managementService.uploadImages(productId, images);
        return ResponseUtils.acceptedResponse(response);
    }

    @PreAuthorize(value = "hasAnyAuthority('CONTENT_CREATOR', 'ADMIN', 'SUPER_ADMIN')")
    @DeleteMapping(value = "custom/product/{productId}/remove")
    public ResponseEntity<ApiResponse> removeImage(@PathVariable(value = "productId") int productId, @RequestParam(value = "image") String imageName) {
        ApiResponse response = managementService.removeImage(productId, imageName);
        return ResponseUtils.acceptedResponse(response);
    }

    @GetMapping(value = "public/product/all")
    public ResponseEntity<ApiResponse> getAllProducts(@Min(value = 1) @RequestParam(value = "size", defaultValue = "10") long pageSize,
                                                      @Min(value = 1) @RequestParam(value = "page", defaultValue = "1") long pageNumber) {
        ApiResponse response = service.getAllProducts(pageSize, pageNumber);
        return ResponseUtils.acceptedResponse(response);
    }

    @GetMapping(value = "public/product/filter")
    public ResponseEntity<ApiResponse> getAllProductsFiltered(@RequestParam(value = "rating-compare", required = false) FilterType ratingFilter,
                                                              @RequestParam(value = "rating-value", required = false) Double ratingValue,
                                                              @RequestParam(value = "price-compare", required = false) FilterType priceFilter,
                                                              @RequestParam(value = "price-value", required = false) Double priceValue,
                                                              @RequestParam(value = "max-price-value", required = false) Double maxPriceValue,
                                                              @RequestParam(value = "category-id-list", required = false) List<Integer> categoryIdList,
                                                              @Min(value = 1) @RequestParam(value = "size", defaultValue = "10") long pageSize,
                                                              @Min(value = 1) @RequestParam(value = "page", defaultValue = "1") long pageNumber) {
        ApiResponse response = service.getAllProductsFiltered(priceValue, maxPriceValue, priceFilter, ratingValue, ratingFilter, categoryIdList, pageSize, pageNumber);
        return ResponseUtils.acceptedResponse(response);
    }

    @GetMapping(value = "public/product/offer/all")
    public ResponseEntity<ApiResponse> getAllOfferProducts(@Min(value = 1) @RequestParam(value = "size", defaultValue = "10") long pageSize,
                                                           @Min(value = 1) @RequestParam(value = "page", defaultValue = "1") long pageNumber) {
        ApiResponse response = service.getAllOfferProducts(pageSize, pageNumber);
        return ResponseUtils.acceptedResponse(response);
    }

    @GetMapping(value = "public/product/price")
    public ResponseEntity<ApiResponse> getAllProductsPriceFilter(@Min(value = 0) @RequestParam(value = "value") double value,
                                                                 @RequestParam(value = "compare") FilterType type,
                                                                 @Min(value = 1) @RequestParam(value = "size", defaultValue = "10") long pageSize,
                                                                 @Min(value = 1) @RequestParam(value = "page", defaultValue = "1") long pageNumber) {
        ApiResponse response = service.getAllProductsPriceFilter(value, type, pageSize, pageNumber);
        return ResponseUtils.acceptedResponse(response);
    }

    @GetMapping(value = "public/product/rate")
    public ResponseEntity<ApiResponse> getAllProductsRateFilter(@Min(value = 0) @RequestParam(value = "value") double value,
                                                                @RequestParam(value = "compare") FilterType type,
                                                                @Min(value = 1) @RequestParam(value = "size", defaultValue = "10") long pageSize,
                                                                @Min(value = 1) @RequestParam(value = "page", defaultValue = "1") long pageNumber) {
        ApiResponse response = service.getAllProductsRateFilter(value, type, pageSize, pageNumber);
        return ResponseUtils.acceptedResponse(response);
    }

    @GetMapping(value = "public/product/{productId}")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable(value = "productId") Integer productId) {
        ApiResponse response = service.getProductById(productId);
        return ResponseUtils.acceptedResponse(response);
    }

    @GetMapping(value = "public/product/category/{categoryId}")
    public ResponseEntity<ApiResponse> getProductsByCategory(@PathVariable(value = "categoryId") Integer categoryId,
                                                             @Min(value = 1) @RequestParam(value = "size", defaultValue = "10") long pageSize,
                                                             @Min(value = 1) @RequestParam(value = "page", defaultValue = "1") long pageNumber) {
        ApiResponse response = service.getProductsByCategory(categoryId, pageSize, pageNumber);
        return ResponseUtils.acceptedResponse(response);
    }

    // TODO: Pagination
    @GetMapping(value = "public/product/main/category/{mainCategoryId}")
    public ResponseEntity<ApiResponse> getProductsByMainCategory(@PathVariable(value = "mainCategoryId") Integer mainCategoryId,
                                                                 @Min(value = 1) @RequestParam(value = "size", defaultValue = "10") long pageSize,
                                                                 @Min(value = 1) @RequestParam(value = "page", defaultValue = "1") long pageNumber) {
        ApiResponse response = service.getProductsByMainCategory(mainCategoryId, pageSize, pageNumber);
        return ResponseUtils.acceptedResponse(response);
    }

    @GetMapping(value = "public/product/min-max-price")
    public ResponseEntity<ApiResponse> getMinMaxPrice() {
        ApiResponse response = service.getMinMaxPrice();
        return ResponseUtils.acceptedResponse(response);
    }

    // TODO: Pagination
    @GetMapping(value = "user/product/all")
    public ResponseEntity<ApiResponse> getAllProductsForUser(@Min(value = 1) @RequestParam(value = "size", defaultValue = "10") long pageSize,
                                                             @Min(value = 1) @RequestParam(value = "page", defaultValue = "1") long pageNumber) {
        ApiResponse response = service.getAllProductsForUser(pageSize, pageNumber);
        return ResponseUtils.acceptedResponse(response);
    }

    @GetMapping(value = "user/product/filter")
    public ResponseEntity<ApiResponse> getAllProductsFilteredForUser(@RequestParam(value = "rating-compare", required = false) FilterType ratingFilter,
                                                                     @RequestParam(value = "rating-value", required = false) Double ratingValue,
                                                                     @RequestParam(value = "price-compare", required = false) FilterType priceFilter,
                                                                     @RequestParam(value = "price-value", required = false) Double priceValue,
                                                                     @RequestParam(value = "max-price-value", required = false) Double maxPriceValue,
                                                                     @RequestParam(value = "category-id-list", required = false) List<Integer> categoryIdList,
                                                                     @Min(value = 1) @RequestParam(value = "size", defaultValue = "10") long pageSize,
                                                                     @Min(value = 1) @RequestParam(value = "page", defaultValue = "1") long pageNumber) {
        ApiResponse response = service.getAllProductsFilteredForUser(priceValue, maxPriceValue, priceFilter, ratingValue, ratingFilter, categoryIdList, pageSize, pageNumber);
        return ResponseUtils.acceptedResponse(response);
    }

    @GetMapping(value = "user/product/offer/all")
    public ResponseEntity<ApiResponse> getAllOfferProductsForUser(@Min(value = 1) @RequestParam(value = "size", defaultValue = "10") long pageSize,
                                                                  @Min(value = 1) @RequestParam(value = "page", defaultValue = "1") long pageNumber) {
        ApiResponse response = service.getAllOfferProductsForUser(pageSize, pageNumber);
        return ResponseUtils.acceptedResponse(response);
    }

    @GetMapping(value = "user/product/price")
    public ResponseEntity<ApiResponse> getAllProductsPriceFilterForUser(@Min(value = 0) @RequestParam(value = "value") double value,
                                                                        @RequestParam(value = "compare") FilterType type,
                                                                        @Min(value = 1) @RequestParam(value = "size", defaultValue = "10") long pageSize,
                                                                        @Min(value = 1) @RequestParam(value = "page", defaultValue = "1") long pageNumber) {
        ApiResponse response = service.getAllProductsPriceFilterForUser(value, type, pageSize, pageNumber);
        return ResponseUtils.acceptedResponse(response);
    }

    @GetMapping(value = "user/product/rate")
    public ResponseEntity<ApiResponse> getAllProductsRateFilterForUser(@Min(value = 0) @RequestParam(value = "value") double value,
                                                                       @RequestParam(value = "compare") FilterType type,
                                                                       @Min(value = 1) @RequestParam(value = "size", defaultValue = "10") long pageSize,
                                                                       @Min(value = 1) @RequestParam(value = "page", defaultValue = "1") long pageNumber) {
        ApiResponse response = service.getAllProductsRateFilterForUser(value, type, pageSize, pageNumber);
        return ResponseUtils.acceptedResponse(response);
    }

    @GetMapping(value = "user/product/{productId}")
    public ResponseEntity<ApiResponse> getProductByIdForUser(@PathVariable(value = "productId") Integer productId) {
        ApiResponse response = service.getProductByIdForUser(productId);
        return ResponseUtils.acceptedResponse(response);
    }

    @GetMapping(value = "user/product/category/{categoryId}")
    public ResponseEntity<ApiResponse> getProductsByCategoryForUser(@PathVariable(value = "categoryId") Integer categoryId,
                                                                    @Min(value = 1) @RequestParam(value = "size", defaultValue = "10") long pageSize,
                                                                    @Min(value = 1) @RequestParam(value = "page", defaultValue = "1") long pageNumber) {
        ApiResponse response = service.getProductsByCategoryForUser(categoryId, pageSize, pageNumber);
        return ResponseUtils.acceptedResponse(response);
    }

    @GetMapping(value = "user/product/main/category/{mainCategoryId}")
    public ResponseEntity<ApiResponse> getProductsByMainCategoryForUser(@PathVariable(value = "mainCategoryId") Integer mainCategoryId,
                                                                        @Min(value = 1) @RequestParam(value = "size", defaultValue = "10") long pageSize,
                                                                        @Min(value = 1) @RequestParam(value = "page", defaultValue = "1") long pageNumber) {
        ApiResponse response = service.getProductsByMainCategoryForUser(mainCategoryId, pageSize, pageNumber);
        return ResponseUtils.acceptedResponse(response);
    }

    @Component
    @RequiredArgsConstructor
    static class StringProductCtoV4Converter implements Converter<String, ProductDtoV4> {
        final ObjectMapper mapper;

        @SneakyThrows
        @Override
        public ProductDtoV4 convert(@NonNull String source) {
            return mapper.readValue(source, ProductDtoV4.class);
        }
    }
}