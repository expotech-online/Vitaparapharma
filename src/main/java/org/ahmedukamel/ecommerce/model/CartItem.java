package org.ahmedukamel.ecommerce.model;

import jakarta.persistence.*;
import lombok.Data;
import org.ahmedukamel.ecommerce.util.OrderItemInterface;

@Data
@Entity
@Table(name = "cart_items")
public class CartItem implements OrderItemInterface {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cartItemId;
    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    @Column(nullable = false)
    private Integer quantity;
}
