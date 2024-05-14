package org.ahmedukamel.ecommerce.controller;

import lombok.RequiredArgsConstructor;
import org.ahmedukamel.ecommerce.dto.response.ApiResponse;
import org.ahmedukamel.ecommerce.service.ReportService;
import org.ahmedukamel.ecommerce.util.ResponseUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "api/v1/admin/report")
public class ReportController {
    private final ReportService service;

    @GetMapping(value = "all")
    public ResponseEntity<ApiResponse> getAllReports() {
        ApiResponse response = service.getAllReports();
        return ResponseUtils.acceptedResponse(response);
    }

    @GetMapping(value = "product/{productId}")
    public ResponseEntity<ApiResponse> getProductReports(@PathVariable(value = "productId") Integer productId) {
        ApiResponse response = service.getProductReports(productId);
        return ResponseUtils.acceptedResponse(response);
    }

    @GetMapping(value = "review/{reviewId}")
    public ResponseEntity<ApiResponse> getReviewReports(@PathVariable(value = "reviewId") Integer reviewId) {
        ApiResponse response = service.getReviewReports(reviewId);
        return ResponseUtils.acceptedResponse(response);
    }

    @GetMapping(value = "to/customer/{customerId}")
    public ResponseEntity<ApiResponse> getReportsToCustomer(@PathVariable(value = "customerId") Integer customerId) {
        ApiResponse response = service.getReportsToCustomer(customerId);
        return ResponseUtils.acceptedResponse(response);
    }

    @GetMapping(value = "from/customer/{customerId}")
    public ResponseEntity<ApiResponse> getReportsFromCustomer(@PathVariable(value = "customerId") Integer customerId) {
        ApiResponse response = service.getReportsFromCustomer(customerId);
        return ResponseUtils.acceptedResponse(response);
    }
}
