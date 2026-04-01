package az.developia.demo.Response;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlaylistResponse {

    private Long id;
    private String title;
    private String description;
    private Double price;
    private Double averageRating;
    private Integer totalReviews;
    private String categoryName;
    private String picture;
}