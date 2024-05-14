package org.ahmedukamel.ecommerce.dto.response.orderCheck;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class OrderCheckResponse {
    private List<ProductOrderCheckResponse> products = new ArrayList<>();
    private double price;
    private boolean couponApplied;
}