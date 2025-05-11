package pl.lodz.dormConnect.database.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.lodz.StudentEntity;

import java.util.Optional;

@Repository
public interface StudentJpaRepository extends JpaRepository<StudentEntity, Long> {
    @Query("""
            select s from StudentEntity s
            join s.user u
            where u.email = :email
            """)
    Optional<StudentEntity> findByEmail(@Param("email") String email);
}
