package org.ahmedukamel.ecommerce.repository;

import org.ahmedukamel.ecommerce.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {
}
