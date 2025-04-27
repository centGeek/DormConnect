package pl.lodz.dormConnect.security.config;

import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import pl.lodz.dormConnect.security.service.JwtService;

import java.io.IOException;
import java.security.Key;

@WebFilter
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final Key secretKey = Keys.hmacShaKeyFor("tajny_klucz_tajny_klucz_tajny_klucz".getBytes());

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = getJwtFromRequest(request);

        if (token != null && jwtService.validateToken(token)) {
            String email = jwtService.getEmailFromToken(token);

            // Utwórz obiekt autentykacji
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    email, null, null);  // Również można ustawić uprawnienia (role)
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // Ustawienie kontekstu bezpieczeństwa
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);  // Kontynuuj filtrację
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);  // Usuwamy "Bearer " z początku
        }
        return null;
    }
}
