package org.ahmedukamel.ecommerce.service.notification;

import org.ahmedukamel.ecommerce.dto.request.NotificationMsgRequest;
import org.ahmedukamel.ecommerce.model.Customer;
import org.ahmedukamel.ecommerce.model.enumeration.NotificationType;

import java.util.List;

public interface NotifyService {
    void notify(NotificationMsgRequest messages, List<Customer> customerList, NotificationType type, String identifier);
}
