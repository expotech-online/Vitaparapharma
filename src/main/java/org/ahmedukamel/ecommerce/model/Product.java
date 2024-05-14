package org.ahmedukamel.ecommerce.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Getter
@Setter
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer productId;
    @Column(nullable = false)
    private Double price;
    @Column(nullable = false)
    private Double rating = .0;
    @Column(nullable = false)
    private Double weight;
    @Column(nullable = false)
    private Double afterDiscount;
    @Column(nullable = false)
    private Boolean discount;
    @Column(nullable = false)
    private Boolean active = true;
    @Column(nullable = false)
    private Integer stockQuantity;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> pictures = new ArrayList<>();
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @JsonIgnore
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProductDetail> productDetails = new HashSet<>();
    @JsonIgnore
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Review> reviews = new HashSet<>();
    @JsonIgnore
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Demand> demands = new HashSet<>();
    @JsonIgnore
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "product_tags",
            inverseJoinColumns = @JoinColumn(name = "tag_id", nullable = false),
            joinColumns = @JoinColumn(name = "product_id", nullable = false))
    private Set<Tag> tags = new HashSet<>();

    public void decrementStockQuantity(Integer stockQuantity) {
        this.stockQuantity -= stockQuantity;
    }

    public void incrementStockQuantity(Integer stockQuantity) {
        this.stockQuantity += stockQuantity;
    }

    public double finalPrice() {
        return this.discount ? this.afterDiscount : this.price;
    }
}
