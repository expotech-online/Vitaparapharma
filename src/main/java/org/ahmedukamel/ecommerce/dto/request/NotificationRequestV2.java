package org.ahmedukamel.ecommerce.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.ahmedukamel.ecommerce.model.enumeration.NotificationType;
import org.ahmedukamel.ecommerce.validation.validators.ValidEnum;

@EqualsAndHashCode(callSuper = true)
@Data
public class NotificationRequestV2 extends NotificationMsgRequest {
    @ValidEnum(enumClass = NotificationType.class)
    private String type;
    private String identifier;
}