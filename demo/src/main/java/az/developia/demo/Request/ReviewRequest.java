package az.developia.demo.Request;

import lombok.Data;

@Data
public class ReviewRequest {
    private Long teacherId;
    private Double rating;
    private String comment;
}