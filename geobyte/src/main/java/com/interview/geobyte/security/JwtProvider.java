package com.interview.geobyte.security;

import com.interview.geobyte.exception.GeoByteException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;
import java.time.Instant;
import java.util.Date;


@Service
public class JwtProvider {

    @Value("${jwt.expiration.time}")
    private Long jwtExpirationTimeInMillis;

    private KeyStore keyStore;

    @PostConstruct
    public void init(){
        try {
            keyStore = KeyStore.getInstance("JKS");
            InputStream resourceAsStream = getClass().getResourceAsStream("/geobyte.jks");
            keyStore.load(resourceAsStream, "byteworks".toCharArray());
        } catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException e) {
            throw new GeoByteException("Exception occurred while loading keystore");
        }
    }

    public String generateToken(Authentication authentication){
        User principal = (User) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject(principal.getUsername())
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plusMillis(jwtExpirationTimeInMillis)))
                .signWith(getPrivateKey())
                .compact();
    }

    public String generateTokenWithEmail(String username){
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plusMillis(getJwtExpirationTimeInMillis())))
                .signWith(getPrivateKey())
                .compact();
    }

    private PrivateKey getPrivateKey(){
        try {
            return (PrivateKey) keyStore.getKey("geobyte", "byteworks".toCharArray());
        } catch (UnrecoverableKeyException | NoSuchAlgorithmException | KeyStoreException e) {
            throw new GeoByteException("Exception occurred while retrieving private key from keystore");
        }
    }

    public boolean validateToken(String token) {
        Jwts.parserBuilder()
                .setSigningKey(getPublicKey())
                .build().parse(token);
        return true;
    }

    private PublicKey getPublicKey() {
        try {
            return keyStore.getCertificate("geobyte").getPublicKey();
        } catch (KeyStoreException e) {
            throw new GeoByteException("Exception occurred while retrieving public key from keystore.");
        }
    }

    public String getEmailFromToken(String token){
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getPublicKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public Long getJwtExpirationTimeInMillis() {
        return jwtExpirationTimeInMillis;
    }
}
