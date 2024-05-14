package org.ahmedukamel.ecommerce.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.ahmedukamel.ecommerce.model.enumeration.NotificationType;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @OneToMany(mappedBy = "notification", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CustomerNotification> customers = new HashSet<>();
    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime timestamp;
    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false, length = 32, columnDefinition = "varchar(32) default 'OTHERS'")
    private NotificationType type = NotificationType.OTHERS;
    private String identifier;

    @JsonIgnore
    @OneToMany(mappedBy = "notification", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<NotificationDetail> notificationDetails = new HashSet<>();
}
