package az.developia.demo.Request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TeacherUpdateRequest {
    private String name;
    private String surname;
    private String phone;
    private String email;
    private String title;
    private String description;

    @JsonProperty("profile_picture_url")
    private String profilePictureUrl;
    private String categoryName;
}
