package az.developia.demo.Repo;

import az.developia.demo.Entity.StudentEntity;
import az.developia.demo.Entity.TeacherEntity;
import az.developia.demo.Entity.UserEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeacherRepo extends JpaRepository<TeacherEntity,Long> {
    Optional<TeacherEntity> findByEmail(String email);
    Optional<TeacherEntity> findByUser(UserEntity user);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM users WHERE email = :email", nativeQuery = true)
    void deleteByEmailNative(@Param("email") String email);
}