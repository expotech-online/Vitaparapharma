package org.ahmedukamel.ecommerce.mapper;

import org.ahmedukamel.ecommerce.dto.response.CouponResponse;
import org.ahmedukamel.ecommerce.model.Coupon;

import java.util.Collection;
import java.util.List;

public class CouponMapper {
    public static CouponResponse toResponse(Coupon coupon) {
        CouponResponse response = new CouponResponse();
        response.setCode(coupon.getCode());
        response.setCreation(coupon.getCreation().toString());
        response.setExpiration(coupon.getExpiration().toString());
        response.setProductId(coupon.getProductId());
        response.setRevoked(coupon.isRevoked());
        response.setDiscount(coupon.getDiscount());
        response.setCouponType(coupon.getCouponType().name());
        response.setDiscountType(coupon.getDiscountType().name());
        return response;
    }

    public static List<CouponResponse> toResponse(Collection<Coupon> coupons) {
        return coupons.stream().map(CouponMapper::toResponse).toList();
    }
}
