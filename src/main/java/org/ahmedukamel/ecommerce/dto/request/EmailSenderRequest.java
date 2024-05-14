package org.ahmedukamel.ecommerce.dto.request;

import lombok.Data;

@Data
public class EmailSenderRequest {
    private String link;
    private String code;
    private String expiration;
    private String[] receivers;
}