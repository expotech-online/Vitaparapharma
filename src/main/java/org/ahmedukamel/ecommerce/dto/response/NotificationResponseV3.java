package org.ahmedukamel.ecommerce.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class NotificationResponseV3 extends NotificationResponse {
    private String identifier;
    private String type;
    private int typeId;
    private int id;
    private boolean read;
}
