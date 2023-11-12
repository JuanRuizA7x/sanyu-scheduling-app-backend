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

    /**
     * Configures the security filter chain for the application.
     *
     * @param httpSecurity          The HttpSecurity object for configuring security.
     * @param authenticationManager The AuthenticationManager for handling authentication.
     * @return A SecurityFilterChain with configured security settings.
     * @throws Exception If an exception occurs during configuration.
     */
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

    /**
     * Configures the authentication manager for the application.
     *
     * @param passwordEncoder The PasswordEncoder for encoding passwords.
     * @return An AuthenticationManager with configured authentication settings.
     */
    @Bean
    public AuthenticationManager authenticationManager(PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(this.userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(authenticationProvider);
    }

    /**
     * Provides a PasswordEncoder for encoding passwords using BCrypt.
     *
     * @return A BCryptPasswordEncoder for password encoding.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Provides a custom encryptor that does not encrypt passwords for easy testing
     *
     * @return A NoOpPasswordEncoder for password encoding.
     */
    //@Bean
    //public PasswordEncoder customPasswordEncoder() {
    //    return new NoOpPasswordEncoder();
    //}

    /**
     * Main method to execute individually for encrypting a specific password.
     * Prints the BCrypt hash of the password for testing or encryption purposes.
     *
     * @param args The command-line arguments (not used).
     */
    //public static void main(String[] args) {
    //    System.out.println("[" + new BCryptPasswordEncoder().encode("12345") + "]");
    //}

}