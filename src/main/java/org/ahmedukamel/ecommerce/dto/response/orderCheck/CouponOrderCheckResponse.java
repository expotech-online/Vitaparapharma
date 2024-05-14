package org.ahmedukamel.ecommerce.dto.response.orderCheck;

import lombok.Data;

@Data
public class CouponOrderCheckResponse {
    private String code;
    private String type;
    private String discountType;
    private double discount;
    private int ProductId;
}
