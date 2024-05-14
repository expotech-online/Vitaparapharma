package org.ahmedukamel.ecommerce.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "customer_notifications")
public class CustomerNotification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;
    @ManyToOne
    @JoinColumn(name = "notification_id", nullable = false)
    private Notification notification;
    @Column(name = "is_read", columnDefinition = "bit(1) default false")
    private boolean read;
}
