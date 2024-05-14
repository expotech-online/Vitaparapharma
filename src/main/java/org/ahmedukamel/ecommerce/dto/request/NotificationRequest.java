package org.ahmedukamel.ecommerce.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.ahmedukamel.ecommerce.validation.annotation.ValidCustomerList;

import java.util.Set;

@Data
public class NotificationRequest {
    @NotBlank
    private String message;
    @ValidCustomerList
    private Set<Integer> customerIdList;
}
