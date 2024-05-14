package org.ahmedukamel.ecommerce.service.coupon;

import org.ahmedukamel.ecommerce.dto.request.CouponRequest;
import org.ahmedukamel.ecommerce.dto.response.ApiResponse;

public interface CouponService {
    ApiResponse addCoupon(CouponRequest request);

    ApiResponse getAllCoupons();

    ApiResponse revokeCoupon(String code);

    ApiResponse invokeCoupon(String code);
}
