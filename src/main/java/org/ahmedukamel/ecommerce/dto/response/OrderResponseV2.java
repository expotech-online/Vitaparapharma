package org.ahmedukamel.ecommerce.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class OrderResponseV2 extends OrderResponse {
    private List<String> coupons;
    private double shipCost;
}