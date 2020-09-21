package com.matiasnnr.market.web.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JWTUtil {

    private static final String KEY = "m4rk3t";

    public String generateToken(UserDetails userDetails){
        return Jwts //La dependencia que implementamos
                //builder nos permite en una secuencia de métodos construír nuestro jwt
                .builder()
                //con incluímos el usuario
                .setSubject(userDetails.getUsername())
                //en qué fecha fue creado el json web token
                .setIssuedAt(new Date())
                //fecha de expiración en 10 horas
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                //firmamos nuestro método con el algorítmo HS256 y usamos la clave KEY definida en la parte
                //superior, esta clave KEY debería estar generada de una manera mucho más compleja
                .signWith(SignatureAlgorithm.HS256, KEY)
                //crea finalmente el JWT y los retorna a partir de los userDetails que tengamos
                .compact();
    }

    //Método para validar si el token que envia el usuario que hace la petición es correcto y no haya expirado
    public boolean validateToken(String token, UserDetails userDetails){
        return userDetails.getUsername().equals(extractUserName(token)) && !isTokenExpired(token);
    }

    //Extraer el username del token
    public String extractUserName(String token){
        //en Subject es donde está el usuario de la petición
        return getClaims(token).getSubject();
    }

    private boolean isTokenExpired(String token){
        //Validamos si la expiración del toquen está antes de la fecha actual y retorna true,
        //sino retorna false el cual sería un token valido aún sin expirar
        return getClaims(token).getExpiration().before(new Date());
    }

    //Claims son como los objetos dentro del JWT
    private Claims getClaims(String token){
        //Le añadimos la llave de la firma al parser y cuando se verifique que la firma es correcta
        return Jwts.parser().setSigningKey(KEY)
                //obtenemos los Claims o el cuerpo del JWT separado por cada uno de los objetos
                .parseClaimsJws(token).getBody();
    }
}


