package pl.lodz.gatewayserver.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements WebFilter {

    private final JwtService jwtService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String token = getJwtFromRequest(exchange.getRequest());

        if (token == null) {
            return chain.filter(exchange);
        }

        try {
            if (jwtService.validateToken(token)) {
                String email = jwtService.getEmailFromToken(token);
                List<SimpleGrantedAuthority> authorities = jwtService.getRolesFromToken(token).stream()
                        .map(role -> role.startsWith("ROLE_") ? new SimpleGrantedAuthority(role)
                                : new SimpleGrantedAuthority("ROLE_" + role))
                        .collect(Collectors.toList());

                var auth = new UsernamePasswordAuthenticationToken(email, null, authorities);
                return chain.filter(exchange)
                        .contextWrite(ReactiveSecurityContextHolder.withAuthentication(auth));
            }
        } catch (Exception e) {
            log.warn("Invalid JWT token: {}", e.getMessage());
            return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token"));
        }

        return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token"));
    }

    private String getJwtFromRequest(ServerHttpRequest request) {
        String bearerToken = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}