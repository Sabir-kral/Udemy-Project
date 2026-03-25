package az.developia.demo.Request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class LoginRequest {
    @Email
    @NotBlank(message = "Bos ola bilmez")
    private String email;

    @NotBlank(message = "Bos ola bilmez")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&]).+$",
            message = "Şifre en azi 1 boyuk, 1 kicik herf, 1 reqem və 1 xususi simvol içermelidir"
    )
    private String password;

}
