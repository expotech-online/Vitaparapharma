package org.ahmedukamel.ecommerce.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity(name = "countries")
public class Country {
    @Id
    private int id;
    @Column(nullable = false)
    private String en;
    @Column(nullable = false)
    private String ar;
    @Column(nullable = false)
    private String fr;
    @OneToMany(mappedBy = "country", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<City> cities = new HashSet<>();
}
