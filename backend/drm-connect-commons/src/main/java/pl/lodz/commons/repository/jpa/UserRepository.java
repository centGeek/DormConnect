package pl.lodz.commons.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.lodz.commons.entity.UserEntity;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    @Query("""
                        select usr from UserEntity usr where usr.email =:email
            """)
    Optional<UserEntity> findByEmail(@Param("email") String email);
}
