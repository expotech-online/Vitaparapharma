package org.ahmedukamel.ecommerce.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.ahmedukamel.ecommerce.validation.annotation.ValidDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
public class CustomerDtoV2 extends CustomerDto {
    private String picture;
    private boolean hasPicture;
    private boolean male = true;
    //    @ValidDateTime
    private String dateOfBirth;
    private String registration;
    private int orders;
    private int createdReports;
    private int reports;
}