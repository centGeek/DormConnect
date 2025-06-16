package pl.lodz.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.lodz.entity.UserEntity;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    @Query("""
                        select usr from UserEntity usr where usr.email =:email
            """)
    Optional<UserEntity> findByEmail(@Param("email") String email);

    @Query("""
        select u from UserEntity u where u.uuid =:uuid
            """)
    Optional<UserEntity> findByUuid(String uuid);

    boolean existsByUserName(String newUsername);
}


