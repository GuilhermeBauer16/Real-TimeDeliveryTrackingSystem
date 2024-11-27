package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.filters;


import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.JwtDetailsService;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.JwtTokenService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@Component
public class JwtValidationFilter extends OncePerRequestFilter {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String AUTHORIZATION_HEADER_BEARER = "Bearer ";
    public static final String INVALID_TOKEN_MESSAGE = "The provided token is invalid!";
    public static final String EXPIRED_TOKEN_MESSAGE = "The provided token is expired!";

    private final JwtTokenService jwtTokenService;
    private final JwtDetailsService jwtDetailsService;

    @Autowired
    public JwtValidationFilter(JwtTokenService jwtTokenService, JwtDetailsService jwtDetailsService) {
        this.jwtTokenService = jwtTokenService;
        this.jwtDetailsService = jwtDetailsService;
    }

    @Override

    protected void doFilterInternal(HttpServletRequest request,  HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        final String header = request.getHeader(AUTHORIZATION_HEADER);
        String username = null;
        String jwt = null;

        if (Objects.nonNull(header) && header.startsWith(AUTHORIZATION_HEADER_BEARER)) {

            jwt = header.substring(AUTHORIZATION_HEADER_BEARER.length());
            try {

                username = jwtTokenService.getUsernameFromToken(jwt);

            } catch (IllegalArgumentException e) {

                response.sendError(HttpServletResponse.SC_BAD_REQUEST, INVALID_TOKEN_MESSAGE);
                return;

            } catch (ExpiredJwtException e) {

                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, EXPIRED_TOKEN_MESSAGE);
                return;

            }

        }

        if (Objects.nonNull(username) && Objects.isNull(SecurityContextHolder.getContext().getAuthentication())) {

            final UserDetails userDetails = jwtDetailsService.loadUserByUsername(username);

            if (Boolean.TRUE.equals(jwtTokenService.validateToken(jwt, userDetails))) {

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        filterChain.doFilter(request, response);

    }
}
