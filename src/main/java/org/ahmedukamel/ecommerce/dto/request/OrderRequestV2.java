package org.ahmedukamel.ecommerce.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class OrderRequestV2 extends OrderRequest {
    String coupon;
}