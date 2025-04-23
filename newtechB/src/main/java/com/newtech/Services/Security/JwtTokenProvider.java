package com.newtech.Services.Security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtTokenProvider {

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    // Create a key during initialization instead of using the string directly
    private Key key;

    @PostConstruct
    public void init() {
        // Either use the existing secret properly
        this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes());

        // OR use the auto-generated key you already have
        // this.key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    }

    @Value("${app.jwtExpirationInMs:86400000}")
    private int jwtExpirationInMs;
    public String generateToken(Authentication authentication) {
        Object principal = authentication.getPrincipal();

        String username;
        Set<String> roles = new HashSet<>();
        if (principal instanceof UserPrincipal) {
            username = ((UserPrincipal) principal).getEmail();
            roles = ((UserPrincipal) principal).getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toSet());
        } else if (principal instanceof org.springframework.security.core.userdetails.User) {
            username = ((org.springframework.security.core.userdetails.User) principal).getUsername();
        } else {
            throw new IllegalStateException("Unexpected principal type: " + principal.getClass().getName());
        }

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        return Jwts.builder()
                .setSubject(username)
                .claim("roles", roles)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key)  // Use the key object
                .compact();
    }


    public String getEmailFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(key)  // Use the key here too
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(key).parseClaimsJws(authToken);  // And here
            return true;
        } catch (SignatureException ex) {
            log.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty");
        }
        return false;
    }
}