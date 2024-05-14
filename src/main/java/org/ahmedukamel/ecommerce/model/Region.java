package org.ahmedukamel.ecommerce.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "regions")
public class Region {
    @Id
    private int id;
    @Column(nullable = false)
    private String en;
    @Column(nullable = false)
    private String ar;
    @Column(nullable = false)
    private String fr;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "city_id", nullable = false)
    private City city;
}
