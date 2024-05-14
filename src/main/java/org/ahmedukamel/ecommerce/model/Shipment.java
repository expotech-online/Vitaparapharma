package org.ahmedukamel.ecommerce.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "shipments")
public class Shipment {
    @Id
    private Integer shipmentId;
    private LocalDateTime shipmentDate;
    private String trackingNumber;
    @OneToOne
    @MapsId
    @JoinColumn(name = "order_id")
    private Order order;
}
