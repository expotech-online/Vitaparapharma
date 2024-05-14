package org.ahmedukamel.ecommerce.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.ahmedukamel.ecommerce.model.enumeration.CouponType;
import org.ahmedukamel.ecommerce.model.enumeration.DiscountType;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "coupons")
public class Coupon {
    @Id
    @Column(updatable = false, length = 20)
    private String code;
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime creation;
    @Column(nullable = false, updatable = false)
    private LocalDateTime expiration;
    private int productId = -1;
    private boolean revoked;
    private double discount;
    @Enumerated(value = EnumType.STRING)
    private CouponType couponType = CouponType.ORDER;
    @Enumerated(value = EnumType.STRING)
    private DiscountType discountType = DiscountType.PERCENTAGE;

    @JsonIgnore
    @OneToMany(mappedBy = "coupon", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CustomerCoupon> customers = new HashSet<>();
}