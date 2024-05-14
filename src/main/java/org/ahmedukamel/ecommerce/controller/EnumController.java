package org.ahmedukamel.ecommerce.controller;

import lombok.RequiredArgsConstructor;
import org.ahmedukamel.ecommerce.dto.response.ApiResponse;
import org.ahmedukamel.ecommerce.service.impl.EnumService;
import org.ahmedukamel.ecommerce.util.ResponseUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api/v2/public/enum")
@RequiredArgsConstructor
public class EnumController {
    final EnumService service;

    @GetMapping(value = "notification-type")
    public ResponseEntity<ApiResponse> getNotificationType() {
        ApiResponse response = service.getNotificationType();
        return ResponseUtils.acceptedResponse(response);
    }

    @GetMapping(value = "coupon-type")
    public ResponseEntity<ApiResponse> getCouponType() {
        ApiResponse response = service.getCouponType();
        return ResponseUtils.acceptedResponse(response);
    }

    @GetMapping(value = "coupon-discount-type")
    public ResponseEntity<ApiResponse> getCouponsDiscountType() {
        ApiResponse response = service.getCouponsDiscountType();
        return ResponseUtils.acceptedResponse(response);
    }

    @GetMapping(value = "order-status")
    public ResponseEntity<ApiResponse> getOrderStatues() {
        ApiResponse response = service.getOrderStatues();
        return ResponseUtils.acceptedResponse(response);
    }

    @GetMapping(value = "advertisement-status")
    public ResponseEntity<ApiResponse> getAdvertisementStatuses() {
        ApiResponse response = service.getAdvertisementStatuses();
        return ResponseUtils.acceptedResponse(response);
    }

    @GetMapping(value = "report-type")
    public ResponseEntity<ApiResponse> getReportTypes() {
        ApiResponse response = service.getReportTypes();
        return ResponseUtils.acceptedResponse(response);
    }

    @GetMapping(value = "role")
    public ResponseEntity<ApiResponse> getRoles() {
        ApiResponse response = service.getRoles();
        return ResponseUtils.acceptedResponse(response);
    }

    @GetMapping(value = "country")
    public ResponseEntity<ApiResponse> getAllCountries() {
        ApiResponse response = service.getAllCountries();
        return ResponseUtils.acceptedResponse(response);
    }
}
