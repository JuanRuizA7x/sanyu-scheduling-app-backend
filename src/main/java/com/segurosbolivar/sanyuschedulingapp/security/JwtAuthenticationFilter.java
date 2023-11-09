package com.segurosbolivar.sanyuschedulingapp.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.segurosbolivar.sanyuschedulingapp.entity.UserEntity;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtProvider jwtProvider;

    public JwtAuthenticationFilter(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    /**
     * Attempt user authentication with username and password.
     *
     * This method is called when a user attempts to authenticate by providing a username and password.
     * It reads the user's credentials from the request, creates an authentication token,
     * and passes it to the authentication manager for verification.
     *
     * @param request The HTTP request containing user credentials.
     * @param response The HTTP response.
     * @return The authentication result after verifying the user's credentials.
     * @throws AuthenticationException If there is an issue with user authentication.
     */
    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws AuthenticationException {

        UserEntity userEntity = null;
        String email = "";
        String password = "";

        try {
            userEntity = new ObjectMapper().readValue(request.getInputStream(), UserEntity.class);
            email = userEntity.getEmail();
            password = userEntity.getPassword();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);

        return getAuthenticationManager().authenticate(authenticationToken);

    }

    /**
     * Handle successful user authentication.
     *
     * This method is called after a user's authentication is successful.
     * It generates a JWT token, prepares the HTTP response,
     * and sends the token along with a success message to the user.
     *
     * @param request The HTTP request.
     * @param response The HTTP response.
     * @param chain The filter chain.
     * @param authResult The authentication result after successful authentication.
     * @throws IOException If there is an issue with I/O operations.
     * @throws ServletException If there is an issue with handling the servlet request.
     */
    @Override
    protected void successfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            Authentication authResult
    ) throws IOException, ServletException {

        User user = (User) authResult.getPrincipal();
        String token = jwtProvider.generateAccessToken(user.getUsername());

        Map<String, Object> httpResponse = new HashMap<>();
        httpResponse.put("message", "Successful authentication");
        httpResponse.put("email", user.getUsername());
        httpResponse.put("token", token);

        response.addHeader("Authorization", token);
        response.getWriter().write(new ObjectMapper().writeValueAsString(httpResponse));
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().flush();

        super.successfulAuthentication(request, response, chain, authResult);

    }

}