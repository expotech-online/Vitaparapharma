package org.ahmedukamel.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.ahmedukamel.ecommerce.validation.annotation.ValidDateTime;
import org.hibernate.validator.constraints.URL;

@Data
public class AdvertisementDto {
    @NotNull
    @URL
    private String targetUrl;
    @ValidDateTime
    private String startDate;
    @ValidDateTime
    private String endDate;
    @NotBlank
    private String title;
    @NotBlank
    private String description;
    // Data for response
    private String imgUrl;
    private String status;
    private Integer adId;
}
