package org.ahmedukamel.ecommerce.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity(name = "cities")
public class City {
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
    @JoinColumn(name = "country_id", nullable = false)
    private Country country;
    @OneToMany(mappedBy = "city", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<Region> regions = new HashSet<>();
}
