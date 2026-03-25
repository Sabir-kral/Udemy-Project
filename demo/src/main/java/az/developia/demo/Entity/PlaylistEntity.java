package az.developia.demo.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Table(name = "playlists")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaylistEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, length = 1000)
    private String description;

    @Column(nullable = false)
    private Double price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", nullable = false)
    private TeacherEntity teacher;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private CategoryEntity category;

    private Double averageRating = 0.0;
    private Integer totalReviews = 0;
}