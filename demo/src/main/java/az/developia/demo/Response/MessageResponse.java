package az.developia.demo.Response;

import lombok.Data;

@Data
public class MessageResponse {

    private String message;
    private String userType;
    private Boolean isVerified;
}