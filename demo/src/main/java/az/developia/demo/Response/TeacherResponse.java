package az.developia.demo.Response;

import az.developia.demo.Entity.ReviewEntity;
import lombok.Data;

import java.util.List;

@Data
public class TeacherResponse {
    private String name;
    private String surname;
    private String phone;
    private String email;
    private String title;
    private String description;
    private String profilePictureUrl;
    private Integer studentCount;
    private List<StudentResponse> students;
    private Double averageRating = 0.0;
    private Integer totalReviews = 0;

}
