package com.matiasnnr.market.web.controller;

import com.matiasnnr.market.domain.dto.AuthenticationRequest;
import com.matiasnnr.market.domain.dto.AuthenticationResponse;
import com.matiasnnr.market.domain.service.MarketUserDetailsService;
import com.matiasnnr.market.web.security.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private MarketUserDetailsService marketUserDetailsService;

    @Autowired
    JWTUtil jwtUtil;

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> createToken(@RequestBody AuthenticationRequest authenticationRequest){

        //Para verificar que el auth se haga bien, lo hacemos dentro de un try/catch
        try {
            //autenticación mediante comprobación de que el user y password son correctos
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authenticationRequest.getUsername(),
                            authenticationRequest.getPassword()
                    ));
            //Una vez la autenticación está lista, vamos a obtener los userDetails
            UserDetails userDetails = marketUserDetailsService.loadUserByUsername(authenticationRequest.getUsername());
            //Ahora generamos el JWT mediante generateToken, pasandole los userDetails
            String jwt = jwtUtil.generateToken(userDetails);

            //va a ocurrir cuando el auth sea correcto y va a retornar el token mediante el sgte return
            return new ResponseEntity<>(new AuthenticationResponse(jwt), HttpStatus.OK);
        } catch (BadCredentialsException e) {
            //BadCredentialsException va a ocurrir cuando el user o password no sean correctas
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

}
