package org.ahmedukamel.ecommerce.dto.request;

import lombok.Data;

@Data
public class PasswordChangeEmailRequest {
    private String requested;
    private String changed;
    private String[] receivers;
}
