package org.ahmedukamel.ecommerce.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.ahmedukamel.ecommerce.validation.annotation.ValidCategory;

import java.util.List;

@Data
public class ProductDtoV4 {
    @NotBlank
    private String englishName;
    @NotBlank
    private String arabicName;
    @NotBlank
    private String frenchName;
    @NotBlank
    private String englishDescription;
    @NotBlank
    private String arabicDescription;
    @NotBlank
    private String frenchDescription;
    @NotBlank
    private String englishAbout;
    @NotBlank
    private String arabicAbout;
    @NotBlank
    private String frenchAbout;
    private boolean isDiscount;
    private boolean isActive;
    @Min(value = 0)
    private double priceBeforeDiscount = -1;
    @Min(value = 0)
    private double priceAfterDiscount;
    @Min(value = 0)
    private double weight = -1;
    @Min(value = 0)
    private int stockQuantity = -1;
    @ValidCategory
    private int categoryId = -1;
    private int productId;
    private List<String> pictures;
    private List<String> englishTags;
    private List<String> arabicTags;
    private List<String> frenchTags;
}