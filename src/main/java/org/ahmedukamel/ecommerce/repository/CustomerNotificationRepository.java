package org.ahmedukamel.ecommerce.repository;

import org.ahmedukamel.ecommerce.model.CustomerNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CustomerNotificationRepository extends JpaRepository<CustomerNotification, Integer> {
}
