package com.example.demo.security;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.security.authentication.BadCredentialsException;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long validityInMilliseconds;

    // Método para crear el token
    public String createToken(String username, Integer userId, String nombreCompleto) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setSubject(username)
                .claim("userId", userId)
                .claim("nombreCompleto", nombreCompleto)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Método para validar el token
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token);  // Si el token es inválido, lanza una excepción
            return true;
      } catch (SecurityException | MalformedJwtException e) {
        throw new BadCredentialsException("JWT incorrecto o manipulado", e);
    // log.warn("JWT manipulado: {}", e.getMessage());  // o usa System.out.println temporalmente
    } catch (ExpiredJwtException e) {
        throw new BadCredentialsException("JWT expirado", e);
    } catch (UnsupportedJwtException e) {
        throw new BadCredentialsException("JWT no soportado", e);
    } catch (IllegalArgumentException e) {
        throw new BadCredentialsException("JWT inválido", e);
    }
    }

    // Método para obtener el username del token
    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // Método para obtener la clave secreta
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }
}
