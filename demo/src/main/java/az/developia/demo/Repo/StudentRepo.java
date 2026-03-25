package az.developia.demo.Repo;

import az.developia.demo.Entity.StudentEntity;
import az.developia.demo.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying; // Required
import org.springframework.data.jpa.repository.Query;    // Required
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface StudentRepo extends JpaRepository<StudentEntity, Long> {
    Optional<StudentEntity> findByEmail(String email);
    Optional<StudentEntity> findByUser(UserEntity user);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM students WHERE email = :email", nativeQuery = true)
    void deleteByEmailNative(@Param("email") String email);
}