package pl.lodz;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.List;

public class JwtService {

    private final Key SECRET_KEY = Keys.hmacShaKeyFor("tajny_klucz_tajny_klucz_tajny_klucz".getBytes(StandardCharsets.UTF_8));


    public String generateToken(Long id, String email, List<String> roles) {
        return Jwts.builder()
                .setSubject(email)
                .claim("id", id)
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getEmailFromToken(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).build().parseClaimsJws(token).getBody().getSubject();
    }

    public List<String> getRolesFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).build().parseClaimsJws(token).getBody();
        return (List<String>) claims.get("roles");
    }

    public Long getIdFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).build().parseClaimsJws(token).getBody();
        return claims.get("id", Long.class);
    }
}
