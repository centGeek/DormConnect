package pl.lodz.dormConnect.database.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.lodz.dormConnect.database.entity.ManagerEntity;
import pl.lodz.dormConnect.database.entity.StudentEntity;

import java.util.Optional;

@Repository
public interface ManagerJpaRepository extends JpaRepository<ManagerEntity, Long> {

    @Query("""
            select man from ManagerEntity man
            join man.user u
            where u.email = :email
            """)
    Optional<ManagerEntity> findByEmail(@Param("email") String email);
}
