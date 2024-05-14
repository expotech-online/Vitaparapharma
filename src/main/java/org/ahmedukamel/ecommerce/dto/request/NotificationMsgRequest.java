package org.ahmedukamel.ecommerce.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class NotificationMsgRequest {
    @NotBlank
    String englishMessage;
    @NotBlank
    String arabicMessage;
    @NotBlank
    String frenchMessage;
}
