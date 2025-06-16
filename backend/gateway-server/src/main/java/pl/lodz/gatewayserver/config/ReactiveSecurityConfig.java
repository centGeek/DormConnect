package pl.lodz.gatewayserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableReactiveMethodSecurity
public class ReactiveSecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public ReactiveSecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {

        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges -> exchanges
                        // Documentation
                        .pathMatchers(
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/swagger-resources/**",
                                "/configuration/ui",
                                "/configuration/security",
                                "/v3/api-docs/**",
                                "/webjars/**",
                                // Microservices
                                "/auth/v3/api-docs/**",
                                "/event/v3/api-docs/**",
                                "/chat/v3/api-docs/**",
                                "/dormProblem/v3/api-docs/**",
                                "/dormService/v3/api-docs/**"
                        ).permitAll()
                        // Users and Auth
                        .pathMatchers(
                                "/api/auth/**",
                                "/register/**",
                                "/api/weather/**"
                        ).permitAll()
                        .pathMatchers(
                                "/api/users/**"
                        ).authenticated()
                        // Events
                        .pathMatchers(
                                "/api/event/administrate/**",
                                "/api/event/administrate"
                        ).hasAnyRole("ADMIN", "MANAGER")
                        .pathMatchers(
                                "/api/event/**"
                        ).authenticated()
                        // Chat
                        .pathMatchers(
                                "/api/chat/**"
                        ).authenticated()
                        // Dorm Problem
                        .pathMatchers(
                                "/api/dorm-problem/**"
                        ).permitAll()
                        // Dorm Service with NFC
                        .pathMatchers(
                                "/api/common-room-assignment/**",
                                "/api/common-room/**",
                                "/api/floors/**",
                                "/api/dorm/form/**",
                                "/api/dorm/room/group/**",
                                "/api/dorm/**", 
                                "/api/nfc/**",
                                "/api/nfc-programmer/**"
                        ).permitAll()
                )
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .addFilterAt(jwtAuthenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }
}