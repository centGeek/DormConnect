package pl.lodz.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "user_profile")

public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "uuid")
    private UUID uuid;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "is_active")
    private boolean isActive;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    private RoleEntity role;

    public UserEntity(long id, String userName, String email, String password, boolean isActive, RoleEntity role) {
        this.id = id;
        this.uuid = UUID.randomUUID();
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.isActive = isActive;
        this.role = role;
    }

    public UserEntity(String userName, String email, String password, boolean isActive, RoleEntity role) {
        this.uuid = UUID.randomUUID();
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.isActive = isActive;
        this.role = role;
    }

    public UserEntity(UUID uuid, String userName, String email, String password, boolean isActive, RoleEntity role) {
        this.uuid = uuid;
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.isActive = isActive;
        this.role = role;
    }
}