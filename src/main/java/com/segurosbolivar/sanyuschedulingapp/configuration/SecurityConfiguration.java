package com.segurosbolivar.sanyuschedulingapp.configuration;

import com.segurosbolivar.sanyuschedulingapp.security.JwtAuthenticationFilter;
import com.segurosbolivar.sanyuschedulingapp.security.JwtAuthorizationFilter;
import com.segurosbolivar.sanyuschedulingapp.security.JwtProvider;
import com.segurosbolivar.sanyuschedulingapp.service.UserDetailsServiceImpl;
import com.segurosbolivar.sanyuschedulingapp.util.NoOpPasswordEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {

    private final String LOGIN_PATH = "/api/auth/login";
    private final String SWAGGER_UI_PATH = "/api/doc/swagger-ui/**";
    private final String SWAGGER_API_DOCS_PATH = "/v3/api-docs/**";
    private final JwtProvider jwtProvider;
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtAuthorizationFilter jwtAuthorizationFilter;

    public SecurityConfiguration(
            JwtProvider jwtProvider,
            UserDetailsServiceImpl userDetailsService,
            JwtAuthorizationFilter jwtAuthorizationFilter
    ) {
        this.jwtProvider = jwtProvider;
        this.userDetailsService = userDetailsService;
        this.jwtAuthorizationFilter = jwtAuthorizationFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity httpSecurity,
            AuthenticationManager authenticationManager
    ) throws Exception {

        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(this.jwtProvider);
        jwtAuthenticationFilter.setAuthenticationManager(authenticationManager);
        jwtAuthenticationFilter.setFilterProcessesUrl(this.LOGIN_PATH);

        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(this.LOGIN_PATH).permitAll()
                        .requestMatchers(this.SWAGGER_UI_PATH).permitAll()
                        .requestMatchers(this.SWAGGER_API_DOCS_PATH).permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilter(jwtAuthenticationFilter)
                .addFilterBefore(this.jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();

    }

    @Bean
    public AuthenticationManager authenticationManager(PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(this.userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(authenticationProvider);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //Encriptador personalizado que devuelve la contraseña sin encriptar para facilitar las pruebas
    /**@Bean
    public PasswordEncoder passwordEncoder() {
        return new NoOpPasswordEncoder();
    }*/

    // Se ejecuta de manera individual para encriptar la contraseña que se desee
    public static void main(String[] args) {
        System.out.println("[" + new BCryptPasswordEncoder().encode("12345") + "]");
    }

}