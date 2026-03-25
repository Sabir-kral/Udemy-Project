package az.developia.demo.Repo;

import az.developia.demo.Entity.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepo extends JpaRepository<ReviewEntity, Long> {

    List<ReviewEntity> findByTeacherId(Long teacherId);

    boolean existsByTeacherIdAndStudentId(Long teacherId, Long studentId);
}