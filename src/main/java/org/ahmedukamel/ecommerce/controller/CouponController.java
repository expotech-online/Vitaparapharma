package org.ahmedukamel.ecommerce.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ahmedukamel.ecommerce.dto.request.CouponRequest;
import org.ahmedukamel.ecommerce.dto.response.ApiResponse;
import org.ahmedukamel.ecommerce.model.enumeration.CouponType;
import org.ahmedukamel.ecommerce.model.enumeration.DiscountType;
import org.ahmedukamel.ecommerce.service.coupon.CouponService;
import org.ahmedukamel.ecommerce.util.ResponseUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "api/v2/admin/coupon")
@RequiredArgsConstructor
public class CouponController {
    private final CouponService couponService;

    @PostMapping(value = "new")
    public ResponseEntity<ApiResponse> addCoupon(@Valid @RequestBody CouponRequest request) {
        ApiResponse response = couponService.addCoupon(request);
        return ResponseUtils.acceptedResponse(response);
    }

    @GetMapping(value = "all")
    public ResponseEntity<ApiResponse> getAllCoupons() {
        ApiResponse response = couponService.getAllCoupons();
        return ResponseUtils.acceptedResponse(response);
    }

    @GetMapping(value = "coupon-types")
    public ResponseEntity<ApiResponse> getCouponTypes() {
        Map<String, String> data = Arrays.stream(CouponType.values()).collect(Collectors.toMap(Enum::name, Enum::name));
        return ResponseUtils.acceptedResponse(new ApiResponse(true, "all coupon types", Map.of("couponTypes", data)));
    }

    @GetMapping(value = "discount-types")
    public ResponseEntity<ApiResponse> getDiscountTypes() {
        Map<String, String> data = Arrays.stream(DiscountType.values()).collect(Collectors.toMap(Enum::name, Enum::name));
        return ResponseUtils.acceptedResponse(new ApiResponse(true, "all discount types", Map.of("discountTypes", data)));
    }

    @DeleteMapping(value = "revoke/{couponCode}")
    public ResponseEntity<ApiResponse> revokeCoupon(@PathVariable(value = "couponCode") String code) {
        ApiResponse response = couponService.revokeCoupon(code);
        return ResponseUtils.acceptedResponse(response);
    }

    @PutMapping(value = "invoke/{couponCode}")
    public ResponseEntity<ApiResponse> invokeCoupon(@PathVariable(value = "couponCode") String code) {
        ApiResponse response = couponService.invokeCoupon(code);
        return ResponseUtils.acceptedResponse(response);
    }
}
