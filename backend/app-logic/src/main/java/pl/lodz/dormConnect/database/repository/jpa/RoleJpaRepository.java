package pl.lodz.dormConnect.database.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.lodz.RoleEntity;

@Repository
public interface RoleJpaRepository extends JpaRepository<RoleEntity, Long> {
    @Query("""
                select role from RoleEntity role where role.roleName =:roleName
            """)
    RoleEntity findByRole(@Param("roleName") String roleName);
}
