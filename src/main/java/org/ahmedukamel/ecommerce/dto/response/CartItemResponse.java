package org.ahmedukamel.ecommerce.dto.response;

import lombok.Data;

@Data
public class CartItemResponse {
    private Integer productId;
    private Integer quantity;
    private Double rating;
    private Double productPrice;
    private Integer stockQuantity;
    private String productName;
    private String pictureUrl;
}