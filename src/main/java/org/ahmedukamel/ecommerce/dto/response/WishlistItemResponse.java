package org.ahmedukamel.ecommerce.dto.response;

import lombok.Data;

@Data
public class WishlistItemResponse {
    private Integer productId;
    private Double productPrice;
    private Double rating;
    private String productName;
    private String productDescription;
    private String pictureUrl;
    private Boolean discount;
    private Double afterDiscount;
}
