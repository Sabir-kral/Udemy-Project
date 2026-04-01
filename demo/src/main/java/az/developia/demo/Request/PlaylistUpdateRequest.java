package az.developia.demo.Request;

import lombok.Data;

@Data
public class PlaylistUpdateRequest {
    private String title;
    private String description;
    private Double price;
    private Long categoryId;
}