package pl.lodz.gatewayserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableReactiveMethodSecurity
public class ReactiveSecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges -> exchanges
                        // Udostępnij Swagger UI i dokumentację publicznie
                        .pathMatchers(
                                "/swagger-ui.html", "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/webjars/**",
                                "/auth/v3/api-docs/**",
                                "/event/v3/api-docs/**"
                        ).permitAll()
                        // Endpointy auth publiczne
                        .pathMatchers("/auth/**", "/auth/api/**", "/auth/register/**").permitAll()
                        // Endpointy event, przykładowe role
                        .pathMatchers("/event/api/event/administrate/**").hasAnyRole("ADMIN", "MANAGER")
                        .pathMatchers("/event/api/event/**").permitAll()
                        // Wszystkie inne wymagają autoryzacji
                        .anyExchange().authenticated()
                )
                // Wyłącz podstawowe formy logowania
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .build();
    }
}