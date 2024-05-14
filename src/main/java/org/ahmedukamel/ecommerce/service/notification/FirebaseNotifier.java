package org.ahmedukamel.ecommerce.service.notification;

public interface FirebaseNotifier {
    void send(String title, String body, String deviceToken);
}