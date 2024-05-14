package org.ahmedukamel.ecommerce.repository;

import jakarta.transaction.Transactional;
import org.ahmedukamel.ecommerce.model.Token;
import org.ahmedukamel.ecommerce.model.enumeration.TokenType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TokenRepository extends JpaRepository<Token, UUID> {
    boolean existsByCode(String code);

    Optional<Token> findByCode(String code);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM Token t WHERE " +
            "t.expiration < :currentDate OR " +
            "t.used = true OR " +
            "t.revoked = true")
    void deleteTokens(@Param(value = "currentDate") Date date);

    void deleteAllByUserId(Integer userId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE Token t SET t.revoked = true WHERE " +
            "t.userId = :userId AND " +
            "t.tokenType = :tokenType")
    void revokeUserTokens(@Param(value = "userId") Integer userId, @Param(value = "tokenType") TokenType tokenType);
}
