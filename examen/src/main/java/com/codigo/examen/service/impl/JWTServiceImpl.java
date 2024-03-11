package com.codigo.examen.service.impl;

import com.codigo.examen.service.JWTService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.function.Function;

@Service
public class JWTServiceImpl implements JWTService {
    @Override
    public String generarToken(UserDetails userDetails) {
        return Jwts.builder().setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 5*36000))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }
    //.signWith(getSignKey(), SignatureAlgorithm.HS256)
    @Override
    public boolean validarToken(String token, UserDetails userDetails) {
        final String username = extactUserName(token);
        return (username.equals(userDetails.getUsername()) && isTokenExpired(token));
    }

    @Override
    public String extactUserName(String token) {
        return extractClaims(token, Claims::getSubject);
    }


//-----------------------------------------------------------

    private String secret = "Cl4v3D1nam1caIs44c";
    /*
    private Key getSignKey(){
        String texto64 = Base64.getEncoder().encodeToString(secret.getBytes());
        byte[] key = Decoders.BASE64.decode(texto64);
        return Keys.hmacShaKeyFor(key);
    }
     private Key getSignKey(){
        byte[] key = Decoders.BASE64.decode("0a1b2c3d4e5f6a7b8c9d0e1f2a3b4c5d6e7f8a9b0c1d2e3f4a5b6c7d8e9f0a1b2c3d4e5f6a");
        return Keys.hmacShaKeyFor(key);
    }
    */

    private <T> T extractClaims(String token, Function<Claims,T> claimsResolver){
        final Claims claims = extactAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extactAllClaims(String token){
            //return Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody();
        return Jwts.parserBuilder().setSigningKey(secret).build().parseClaimsJws(token).getBody();
    }

    private boolean isTokenExpired(String token){
        //return extractClaims(token, Claims::getExpiration).before(new Date());
        return (new Date()).before(extractClaims(token, Claims::getExpiration));
    }


}
