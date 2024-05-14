package org.ahmedukamel.ecommerce.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class ProductDtoV3 extends ProductDto {
    private Integer mainCategoryId;
    List<String> tags;
}