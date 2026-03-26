package az.developia.demo.Response;

import lombok.Data;

@Data
public class UserResponse {
    private Long id;

    private String email;
    private String password;
    private String userType;
    private Double balance;
}
