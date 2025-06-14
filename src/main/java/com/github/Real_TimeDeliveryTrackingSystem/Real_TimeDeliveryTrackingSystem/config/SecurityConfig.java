package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.config;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.filters.CsrfCookieFilter;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.filters.JwtValidationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

    private static final String[] DRIVER_RESOURCES = {"/vehicle/**", "/driver/**", "/driverAddress/**"};
    private static final String[] CUSTOMER_RESOURCES = {"/customer/**", "/customer/product/**", "/shoppingCart/**",
            "/payment/v1/checkout/**", "index/**", "/mercadoPago/**"};

    private static final String[] CSRF_IGNORE_REQUEST_MATCHER = {"/vehicle/**", "/api/login", "/signInCustomer", "/customer/**",
            "/signInDriver/**", "/driver/**", "/driverAddress/**", "/customerAddress/**", "/verificationCode/**", "/product/**"
            , "/customer/product/**", "/shoppingCart/**", "/payment/v1/checkout/**", "index/**", "/mercadoPago/**","/ipn/**"};
    private static final String[] ADMIN_RESOURCES = {"/product/**"};
    private static final String ROLE_ADMIN = "ADMIN";
    private static final String ROLE_CUSTOMER = "CUSTOMER";
    private static final String ROLE_DRIVER = "DRIVER";


    @Bean
    @Autowired
    SecurityFilterChain securityFilterChain(HttpSecurity http, JwtValidationFilter jwtValidationFilter) throws Exception {
        CsrfTokenRequestAttributeHandler requestHandler = new CsrfTokenRequestAttributeHandler();
        requestHandler.setCsrfRequestAttributeName("_csrf");

        return http
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(csrf -> csrf.csrfTokenRequestHandler(requestHandler)
                        .ignoringRequestMatchers(CSRF_IGNORE_REQUEST_MATCHER)
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class)
                .addFilterAfter(jwtValidationFilter, BasicAuthenticationFilter.class)
                .sessionManagement(
                        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(
                        authorizeHttpRequests -> authorizeHttpRequests
//
                                .requestMatchers(DRIVER_RESOURCES).hasRole(ROLE_DRIVER)
                                .requestMatchers(CUSTOMER_RESOURCES).hasRole(ROLE_CUSTOMER)
                                .requestMatchers(ADMIN_RESOURCES).hasRole(ROLE_ADMIN)
                                .anyRequest().permitAll()


                )
//                .headers(headers -> headers
//                        .contentSecurityPolicy(csp -> csp.policyDirectives(
//                                "default-src 'self'; " +
//                                        "script-src 'self' 'unsafe-inline' 'unsafe-eval' blob: " +
//                                        "*.mercadopago.com mercadopago.com.br *.mercadopago.com.br " +
//                                        "hotjar.com *.hotjar.com static.hotjar.com *.static.hotjar.com script.hotjar.com *.script.hotjar.com; " +
//                                        "connect-src 'self' " +
//                                        "*.mercadopago.com mercadopago.com.br *.mercadopago.com.br " +
//                                        "*.hotjar.com static.hotjar.com script.hotjar.com;" // 🔹 Permite conexões
//                        )))

                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));

        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource =
                new UrlBasedCorsConfigurationSource();

        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", configuration);
        return urlBasedCorsConfigurationSource;
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();

    }
}
