package com.matiasnnr.market.domain.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class MarketUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //user,
        // password(como la pass no ha pasado por un decoder es necesario poner {noop} antes),
        // tipos de roles que tendrá el user
        return new User("marketuser", "{noop}market123", new ArrayList<>());
    }
}
