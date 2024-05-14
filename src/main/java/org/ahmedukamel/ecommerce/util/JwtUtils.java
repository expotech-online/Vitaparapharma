package org.ahmedukamel.ecommerce.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

public class JwtUtils {
    private static final String signingKey = "895cd3afcc506bc72c1e5ea140d2ba3baf7d4a7c474e2ee883138d081a9529343f2c4522b2573d570c0fa6d81bd15168a0dd6c3433b46c1973eff0fe573bbc7329b94712e84221e3b1ecd34541231503b0c8d8df7a2c0d03449d2d6128846851460960a30655de472ae5f59cca16b411ffb0cb6b39ddca20579769e901a54423";
    private static final long ONE_DAY = 1_000 * 60 * 60 * 24;
    private static final long ONE_WEEK = ONE_DAY * 7;

    public static String getEmail(String token) {
        return getClaim(token.strip(), Claims::getSubject);
    }

    public static String getProvider(String token) {
        Claims claims = getClaims(token);
        return claims.get("provider") + "";
    }

    public static boolean isValid(String token, UserDetails userDetails) {
        final String email = getEmail(token);
        return email.equals(userDetails.getUsername()) && !isExpired(token);
    }

    public static boolean isExpired(String token) {
        return getExpiration(token).before(new Date());
    }

    public static String generateToken(UserDetails userDetails, Boolean rememberMe) {
        return generateToken(userDetails, rememberMe, Map.of());
    }

    public static String generateToken(UserDetails userDetails, Boolean rememberMe, Map<String, Object> extraClaims) {
        long issuedAt = System.currentTimeMillis();
        long expireAt = issuedAt + (rememberMe ? ONE_WEEK : ONE_DAY);
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(issuedAt))
                .setExpiration(new Date(expireAt))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private static Date getExpiration(String token) {
        return getClaim(token, Claims::getExpiration);
    }

    private static <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getClaims(token);
        return claimsResolver.apply(claims);
    }

    private static Claims getClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
    }

    private static SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(signingKey));
    }
}