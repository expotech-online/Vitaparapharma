package org.ahmedukamel.ecommerce.dto.response.orderCheck;

import lombok.Data;

@Data
public class ProductOrderCheckResponse {
    private int id;
    private int quantity;
    private String name;
    private double price;
    private boolean discount;
}
