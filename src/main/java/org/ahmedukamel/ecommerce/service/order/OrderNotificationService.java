package org.ahmedukamel.ecommerce.service.order;

import org.ahmedukamel.ecommerce.model.Order;
import org.ahmedukamel.ecommerce.model.enumeration.OrderStatus;

public interface OrderNotificationService {
    void notify(Order order, OrderStatus status);
}
