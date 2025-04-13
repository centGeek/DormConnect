package pl.lodz.dormConnect.security.config;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.lodz.dormConnect.security.model.RoleEntity;
import pl.lodz.dormConnect.security.model.UserEntity;
import pl.lodz.dormConnect.security.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserSecurityConfig implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByEmail(email);

        if (user == null) {
            throw new UsernameNotFoundException("User with email " + email + " not found");
        }

        SimpleGrantedAuthority authorities = getUserAuthority(user.getRole());

        return buildUserForAuthentication(user, List.of(authorities));
    }

    private SimpleGrantedAuthority getUserAuthority(RoleEntity roleEntity) {
        return new SimpleGrantedAuthority(roleEntity.getRoleName());
    }

    private UserDetails buildUserForAuthentication(
            UserEntity user,
            List<SimpleGrantedAuthority> authorities
    ) {
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                user.isActive(),
                true,
                true,
                true,
                authorities
        );
    }
}
