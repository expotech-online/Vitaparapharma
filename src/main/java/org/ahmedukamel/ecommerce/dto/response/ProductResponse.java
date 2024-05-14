package org.ahmedukamel.ecommerce.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class ProductResponse {
    private String name;
    private String description;
    private String about;
    private int productId;
    private int categoryId;
    private int mainCategoryId;
    private int stockQuantity;
    private int reviews;
    private double priceBeforeDiscount;
    private double priceAfterDiscount;
    private double rating;
    private double weight;
    private boolean isDiscount;
    private boolean isInWishlist;
    private boolean isInCart;
    private List<String> pictures;
    private List<String> tags;
}