package org.ahmedukamel.ecommerce.dto.response;

import lombok.Data;

@Data
public class OrderItemResponse {
    private Integer productId;
    private Integer quantity;
    private Double unitPrice;
    private Double totalPrice;
    private String productName;
    private String pictureUrl;
}
