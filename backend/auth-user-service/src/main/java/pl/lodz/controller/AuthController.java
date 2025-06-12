package pl.lodz.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.config.JwtService;
import pl.lodz.entity.UserEntity;
import pl.lodz.repository.jpa.UserRepository;

import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth/api")
@AllArgsConstructor
@Slf4j
public class AuthController {

    private AuthenticationManager authenticationManager;
    private JwtService jwtService;

    private UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request, HttpServletResponse response) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = (User) authentication.getPrincipal();
        Optional<UserEntity> byEmail = userRepository.findByEmail(user.getUsername());
        String token = jwtService.generateToken(byEmail.get().getId(), user.getUsername(), user.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList())
        );

        Cookie cookie = new Cookie("token", token);
        cookie.setPath("/");
        cookie.setMaxAge(3600);

        response.addCookie(cookie);

        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {

        Cookie cookie = new Cookie("token", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);

        return ResponseEntity.ok("Successfully logged out");
    }


    @Data
    public static class AuthRequest {
        private String email;
        private String password;
    }

    @Data
    @AllArgsConstructor
    public static class AuthResponse {
        private String token;
    }

}
