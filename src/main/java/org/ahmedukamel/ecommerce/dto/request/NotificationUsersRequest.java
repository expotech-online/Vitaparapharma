package org.ahmedukamel.ecommerce.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class NotificationUsersRequest extends NotificationRequestV2 {
    @NotEmpty
    private List<Integer> customerIdList;
}
