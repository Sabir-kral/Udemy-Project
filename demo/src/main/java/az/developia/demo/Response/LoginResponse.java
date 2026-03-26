package az.developia.demo.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private Long id;
    private String email;
    private String userType;
    private String accessToken;
    private Double balance;
}
