package org.ahmedukamel.ecommerce.model;

import jakarta.persistence.*;
import lombok.Data;
import org.ahmedukamel.ecommerce.model.enumeration.TokenType;

import java.util.Date;
import java.util.UUID;

@Data
@Entity
@Table(name = "tokens")
public class Token {
    @Id
    @GeneratedValue
    private UUID id;
    @Column(nullable = false)
    private String code;
    @Column(nullable = false)
    private Integer userId;
    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private TokenType tokenType;
    @Column(nullable = false)
    private Date creation;
    @Column(nullable = false)
    private Date expiration;
    @Column(nullable = false)
    private Boolean used;
    @Column(nullable = false)
    private Boolean revoked;
}
