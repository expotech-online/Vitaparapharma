package org.ahmedukamel.ecommerce.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.ahmedukamel.ecommerce.model.enumeration.CouponType;
import org.ahmedukamel.ecommerce.model.enumeration.DiscountType;
import org.ahmedukamel.ecommerce.validation.annotation.ValidDateTime;
import org.ahmedukamel.ecommerce.validation.validators.ValidEnum;

import java.util.ArrayList;
import java.util.List;

@Data
public class CouponRequest {
    @ValidDateTime
    private String expiration;
    private int productId;
    @Min(value = 0)
    private double discount = -1;
    @NotNull
    private CouponType couponType;
    @NotNull
    private DiscountType discountType;
    @NotBlank
    private String englishMessage;
    @NotBlank
    private String arabicMessage;
    @NotBlank
    private String frenchMessage;
    private List<Integer> customers = new ArrayList<>();
}