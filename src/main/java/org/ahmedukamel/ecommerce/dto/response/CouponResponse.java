package org.ahmedukamel.ecommerce.dto.response;

import lombok.Data;

@Data
public class CouponResponse {
    private String code;
    private String creation;
    private String expiration;
    private int productId;
    private boolean revoked;
    private double discount;
    private String couponType;
    private String discountType;
}
