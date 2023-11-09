package com.segurosbolivar.sanyuschedulingapp.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtProvider {

    @Value("${jwt.secret-key}")
    private String secretKey;
    @Value("${jwt.expiration-time}")
    private String expirationTime;

    /**
     * Generate an access token for a user.
     *
     * This method creates a JWT access token for the given user's email.
     * The token contains information about the subject (user email), issuance date, expiration date, and is signed with a secret key.
     *
     * @param userEmail The email of the user for whom the access token is generated.
     * @return A JWT access token as a string.
     */
    public String generateAccessToken(String userEmail) {
        return Jwts.builder()
                .setSubject(userEmail)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(expirationTime)))
                .signWith(getSignatureKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Get the signature key used for JWT signing.
     *
     * This method retrieves the signature key for JWT token signing, generated from the secret key.
     *
     * @return The JWT signature key.
     */
    public Key getSignatureKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Validate the authenticity of an access token.
     *
     * This method validates whether an access token is genuine by verifying its signature and expiration.
     *
     * @param token The access token to be validated.
     * @return True if the token is valid, false otherwise.
     */
    public boolean validateAccessToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSignatureKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Extract all claims from a JWT token.
     *
     * This method extracts all the claims (payload) from a JWT token.
     *
     * @param token The JWT token from which to extract claims.
     * @return All claims from the token.
     */
    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignatureKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Get a specific claim from a JWT token.
     *
     * This method retrieves a specific claim from a JWT token by applying a custom function to the token's claims.
     *
     * @param token The JWT token from which to extract the claim.
     * @param claimsTFunction The function to extract the desired claim.
     * @param <T> The type of the claim.
     * @return The specific claim extracted from the token.
     */
    public <T> T getClaim(String token, Function<Claims, T> claimsTFunction) {
        Claims claims = extractAllClaims(token);
        return claimsTFunction.apply(claims);
    }

    /**
     * Get the user's email from a JWT token.
     *
     * This method retrieves the user's email from a JWT token's claims.
     *
     * @param token The JWT token from which to extract the user's email.
     * @return The user's email.
     */
    public String getUserEmailFromToken(String token) {
        return getClaim(token, Claims::getSubject);
    }

}