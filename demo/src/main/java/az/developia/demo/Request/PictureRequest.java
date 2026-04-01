package az.developia.demo.Request;

import lombok.Data;
import org.hibernate.validator.constraints.URL;

@Data
public class PictureRequest {
    @URL
    private String url;
}