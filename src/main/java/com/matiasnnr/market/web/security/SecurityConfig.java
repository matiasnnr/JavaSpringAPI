package com.matiasnnr.market.web.security;

import com.matiasnnr.market.domain.service.MarketUserDetailsService;
import com.matiasnnr.market.web.security.filter.JwtFilterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtFilterRequest jwtFilterRequest;

    @Autowired
    private MarketUserDetailsService marketUserDetailsService;

    //Le indicamos con que user y pass queremos entrar
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(marketUserDetailsService);
    }

    //Le indicamos que autorice todas las peticiones que se hagan a @PostMapping("/authenticate") en AuthController
    //porque para invocar a este servicio no necesitamos estar autenticados, ya que con ese servicio es con el que
    //nos autenticamos realmente
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests()
                //con antMatchers le decimos que es lo que queremos permitir
                //Esto quiere decir que todas las peticiones que terminen en /authenticate, las permita
                .antMatchers("/**/authenticate").permitAll()
                //ahora decimos que para todas las demas peticiones si debemos estar autenticados
                .anyRequest().authenticated()
                //le decimos cual filtro será en encargado de recibir todas las peticiones y procesarlas
                .and().sessionManagement()
                //aquí indicamos que la sesión que vamos a utilizar dentro de nuestra aplicación será stateless
                //(sin sesión) porque los jwt son los que van a controlar cada petición en particular, sin
                //manejar una sesión como tal
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        //agregamos el filtro y le decimos que el tipo de filtro es de tipo usuario y contraseña
        http.addFilterBefore(jwtFilterRequest, UsernamePasswordAuthenticationFilter.class);
    }

    //Como desde AuthController estabamos usando authenticationManager, entonces tb debemos incuirlo acá
    //Este método indica que sea Spring quien siga controlando la gestión de autenticación
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    //Para permitir la visualización de Swagger públicamente sin tener que estar autenticados en la aplicación
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/v2/api-docs", "/configuration/ui",
                "/swagger-resources/**", "/configuration/security",
                "/swagger-ui.html", "/webjars/**");
    }

}
