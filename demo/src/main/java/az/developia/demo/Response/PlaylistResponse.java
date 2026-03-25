package az.developia.demo.Response;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlaylistResponse {
    private Long id;
    private String title;
    private String description;
    private Double price;
    private Long categoryId;
    private Long teacherId;
    private Double averageRating;
    private Integer totalReviews;
}