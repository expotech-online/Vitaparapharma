package org.ahmedukamel.ecommerce.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class ProductDtoV2 extends ProductDto {
    @NotBlank
    private String arabicName;
    @NotBlank
    private String arabicDescription;
    @NotBlank
    private String arabicAbout;
    @NotBlank
    private String frenchName;
    @NotBlank
    private String frenchDescription;
    @NotBlank
    private String frenchAbout;
    @NotNull
    @Min(value = 0)
    private Double weight;
}