package org.ahmedukamel.ecommerce.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
public class Tag {
    @Id
    private String name;

    @ManyToOne
    @JoinColumn(name = "language_id", nullable = false, updatable = false)
    private Language language;

    public Tag(String name) {
        this.name = name;
    }

    @ManyToMany(mappedBy = "tags", fetch = FetchType.EAGER)
    private Set<BlogPost> posts = new HashSet<>();

    @ManyToMany(mappedBy = "tags", fetch = FetchType.EAGER)
    private Set<Product> products = new HashSet<>();
}