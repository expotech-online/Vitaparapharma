package org.ahmedukamel.ecommerce.dto.response;

import lombok.Data;
import org.ahmedukamel.ecommerce.model.enumeration.CouponType;
import org.ahmedukamel.ecommerce.model.enumeration.DiscountType;

@Data
public class CustomerCouponResponse {
    private int orderId;
    private int productId;
    private double discount;
    private boolean used;
    private boolean expired;
    private boolean revoked;
    private String couponCode;
    private CouponType couponType;
    private DiscountType discountType;
}