package az.developia.demo.Repo;

import az.developia.demo.Entity.TeacherEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeacherRepo extends JpaRepository<TeacherEntity,Long> {
}
