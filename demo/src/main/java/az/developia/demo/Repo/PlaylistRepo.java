package az.developia.demo.Repo;

import az.developia.demo.Entity.PlaylistEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaylistRepo extends JpaRepository<PlaylistEntity,Long> {
    List<PlaylistEntity> findByCategoryId(Long categoryId);
    List<PlaylistEntity> findByTeacherId(Long teacherId);
}
