package org.ahmedukamel.ecommerce.model.enumeration;

import lombok.Getter;

@Getter
public enum NotificationType {
    COUPONS(1),
    OFFERS(2),
    DEMANDS(3),
    ORDERS(4),
    OTHERS(5);

    private final int id;

    NotificationType(int id) {
        this.id = id;
    }
}