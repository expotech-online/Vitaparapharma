package org.ahmedukamel.ecommerce.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "wishlists")
public class Wishlist {
    @Id
    private Integer wishlistId;
    @OneToOne
    @MapsId
    @JoinColumn(name = "customer_id")
    private Customer customer;
    @JsonIgnore
    @OneToMany(mappedBy = "wishlist", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WishlistItem> wishlistItems = new ArrayList<>();
}
