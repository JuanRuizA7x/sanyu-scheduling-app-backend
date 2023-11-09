package com.segurosbolivar.sanyuschedulingapp.security;

import com.segurosbolivar.sanyuschedulingapp.service.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final UserDetailsServiceImpl userDetailsService;

    public JwtAuthorizationFilter(
            JwtProvider jwtProvider,
            UserDetailsServiceImpl userDetailsService
    ) {
        this.jwtProvider = jwtProvider;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Perform authorization based on JWT token.
     *
     * This method is called to perform authorization based on the JWT token provided in the HTTP request's "Authorization" header.
     * If a valid JWT token is found, it is used to authenticate the user and set the user's security context.
     *
     * @param request The HTTP request.
     * @param response The HTTP response.
     * @param filterChain The filter chain for processing the request.
     * @throws ServletException If there is an issue with handling the servlet request.
     * @throws IOException If there is an issue with I/O operations.
     */
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        String tokenHeader = request.getHeader("Authorization");

        if(tokenHeader != null && tokenHeader.startsWith("Bearer ")) {

            String token = tokenHeader.replace("Bearer ", "");

            if(this.jwtProvider.validateAccessToken(token)) {
                String email = jwtProvider.getUserEmailFromToken(token);
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);

                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(email, null, userDetails.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }

        }

        filterChain.doFilter(request, response);

    }

}