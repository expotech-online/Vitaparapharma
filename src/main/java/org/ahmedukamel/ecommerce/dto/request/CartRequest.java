package org.ahmedukamel.ecommerce.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.ahmedukamel.ecommerce.validation.annotation.ValidProduct;

@Data
public class CartRequest {
    @ValidProduct
    private Integer productId;
    @NotNull
    @Min(value = 1)
    private Integer quantity;
}
