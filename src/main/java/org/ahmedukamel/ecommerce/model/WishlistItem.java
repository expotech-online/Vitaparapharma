package org.ahmedukamel.ecommerce.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "wishlist_items")
public class WishlistItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer wishlistItemId;
    @ManyToOne
    @JoinColumn(name = "wishlist_id", nullable = false)
    private Wishlist wishlist;
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime dateAdded;
}
