package org.ahmedukamel.ecommerce.dto.request;

import lombok.Data;
import org.ahmedukamel.ecommerce.validation.annotation.ValidAdvertisement;
import org.ahmedukamel.ecommerce.validation.annotation.ValidAdvertisementStatus;

@Data
public class AdStatusRequest {
    @ValidAdvertisement
    private Integer advertisementId;
    @ValidAdvertisementStatus
    private String advertisementStatus;
}
