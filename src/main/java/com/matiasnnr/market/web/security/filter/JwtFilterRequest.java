package com.matiasnnr.market.web.security.filter;

import com.matiasnnr.market.domain.service.MarketUserDetailsService;
import com.matiasnnr.market.web.security.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//Agregamos extends OncePerRequestFilter para que este filtro se ejecute cada vez que haya una petición
@Component
public class JwtFilterRequest extends OncePerRequestFilter {

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private MarketUserDetailsService marketUserDetailsService;

    //En este método vamos a verificar si lo que viene en el encabezado de la petición es un token y si es correcto
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer")){
            //Desde la posición 7 es que viene el token, despues de "Bearer "
            String jwt = authorizationHeader.substring(7);
            //Ahora verificamos el user de ese jwt
            String username = jwtUtil.extractUserName(jwt);

            //Verificamos a user != null y tb que en el contexto aún no exista ninguna authentication para este usuario
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null){
                //a través de este servicio verificamos que este user existe dentro de nuestro system de authentication
                UserDetails userDetails = marketUserDetailsService.loadUserByUsername(username);

                //verificamos si jwt es correcto
                if (jwtUtil.validateToken(jwt, userDetails)){
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities()
                    );

                    //Con esto se puede validar que navegador estaba usando, horario de conexión, OS, etc.
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    //Aquí le asignamos la authentication para que la próxima vez no tenga que pasar
                    //otra vez por toda la validación de este filtro, gracias a la validación de más arriba
                    //if (username != null && SecurityContextHolder.getContext().getAuthentication() == null){
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
        }
        //hacemos que el filtro sea evaluado
        filterChain.doFilter(request, response);
    }
}
