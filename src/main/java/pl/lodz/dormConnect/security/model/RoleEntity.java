package pl.lodz.dormConnect.security.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "role")
@AllArgsConstructor
public class RoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long id;
    @Column(name = "role_name")
    private String roleName;

    public RoleEntity(String roleName) {
        this.roleName = roleName;
    }
}