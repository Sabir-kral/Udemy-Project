package az.developia.demo.Repo;

import az.developia.demo.Entity.RoleEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepo  extends JpaRepository<RoleEntity,Long> {
    @Transactional
    @Modifying
    @Query(value = "insert into user_roles(user_id, role_id) select ?1, id from roles where student = 1", nativeQuery = true)
    void assignStudentRoles(Long userId);
    @Transactional
    @Modifying
    @Query(value = "insert into user_roles(user_id, role_id) select ?1, id from roles where teacher = 1", nativeQuery = true)
    void assignTeacherRoles(Long userId);

    @Transactional
    @Modifying
    @Query(value = "insert into user_roles(user_id, role_id) select ?1, id from roles where admin = 1", nativeQuery = true)
    void assignAdminRoles(Long userId);
}
