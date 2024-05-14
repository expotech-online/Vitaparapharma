package org.ahmedukamel.ecommerce.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "product_details")
public class ProductDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name = "";
    @Column(columnDefinition = "TEXT")
    private String description = "";
    @Column(columnDefinition = "TEXT")
    private String about = "";
    @ManyToOne
    @JoinColumn(name = "language_id", nullable = false)
    private Language language;
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
}
