package pl.lodz;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    @Query("""
                        select usr from UserEntity usr where usr.email =:email
            """)
    Optional<UserEntity> findByEmail(@Param("email") String email);
}
