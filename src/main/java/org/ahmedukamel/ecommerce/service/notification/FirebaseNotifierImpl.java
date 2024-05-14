package org.ahmedukamel.ecommerce.service.notification;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import lombok.RequiredArgsConstructor;
import com.google.firebase.messaging.Notification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FirebaseNotifierImpl implements FirebaseNotifier {
    final FirebaseMessaging firebaseMessaging;

    @Override
    public void send(String title, String body, String deviceToken) {
        Notification notification = Notification
                .builder()
                .setTitle(title)
                .setBody(body)
                .build();

        Message message = Message
                .builder()
                .setToken(deviceToken)
                .setNotification(notification)
                .build();

        try {
            firebaseMessaging.send(message);
        } catch (FirebaseMessagingException ignore) {
        }
    }
}